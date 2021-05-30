/*
***
***
***   INCLUDES
***
***
***
 */

#include <ESP8266WiFi.h>
#include <PubSubClient.h>

//Network Time Protocol (NTP) es un protocolo de Internet para sincronizar los relojes de los sistemas informáticos
//a través del enrutamiento de paquetes en redes con latencia variable. NTP utiliza UDP como su capa de transporte,
//usando el puerto 123. Está diseñado para resistir los efectos de la latencia variable.
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <Servo.h>

#include "ArduinoJson.h"
#include "RestClient.h"
#include "config.h"

/*
***
***
***   CABECERAS DE FUNCIONES
***
***
***
 */

//Establecemos connection wifi
void setup_wifi();

//Callback de MQTT
void callback(char *, byte *, unsigned int);

//Para escribir JSON  de BoardProduction
String serializeBoardProduction(int, int, int, String, float);

//Para escribir JSON de Log
String serializeLog(int, String, String);

//Para leer JSON de Posiciones de Servos
void deserializePosition(String, String);

//Para leer JSON de SunPosition
void deserializeSunPosition(String);

//Para leer JSON Dde Board
void deserializeBoard(String);

//Para mover los servos
void moveServos(float, float);

//GET a SunPosition
void GET_SunPosition();

//POST a Log
void POST_LOG();

//POST a BoardProduction
void POST_BoardProduction();

/*
***
***
***   Variables y Instancias
***
***
***
 */

WiFiClient espClient;
PubSubClient Mqttclient(espClient);
RestClient Restclient = RestClient(mqtt_server, 8089);

//servo Azimut
Servo myservoA;
//Servo Elevation
Servo myservoE;

//Time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org"); //CSV must be UCT (+0) No DST

//NOSE SI LO VAMOS A USAR PENDIENTE DE BORRAR
long lastMsg = 0;
char msg[50];

int test_delay = 1000; //so we don't spam the API
boolean describe_tests = true;

//Para almacenar el Code de la placa
char const *code;
//Para almacenar la posicion de la placa
int position;

//Para guardar el resultado de los GET
String response;

float maxProduction;

/*
***
***
***   SETUP
***
***
***
 */
void setup()
{
  //Nose si nos sirve
  pinMode(LED_BUILTIN, OUTPUT);
  //Pines de salida para los servos
  pinMode(D2, OUTPUT);
  pinMode(D3, OUTPUT);

  Serial.begin(115200);

  //Llamamos la funcion para establecer el wifi
  setup_wifi();

  //Mqtt client con el puerto y la funcion callback
  Mqttclient.setServer(mqtt_server, 1883);
  Mqttclient.setCallback(callback);

  //Establecemos los pines D2 y D3 para la señal de los servos.
  myservoA.attach(D2);
  myservoE.attach(D3);
  //Time
  timeClient.begin();

  //setup board
  //GET_board();
}

void setup_wifi()
{
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

/*
***
***
***   MQTT
***
***
***
 */

void callback(char *topic, byte *payload, unsigned int length)
{

  String JsonObject = "";

  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");

  for (unsigned int i = 0; i < length; i++)
  {
    JsonObject.concat((char)payload[i]);
    Serial.print((char)payload[i]);
  }
  Serial.println();

  if ((String)topic == "servo/manual/A")
  {
    deserializePosition(JsonObject, "A");
  }
  else if ((String)topic == "servo/manual/E")
  {
    deserializePosition(JsonObject, "E");
  }

  if ((char)payload[0] == '1')
  {
    digitalWrite(LED_BUILTIN, LOW);
  }
  else
  {
    digitalWrite(LED_BUILTIN, HIGH); // Turn the LED off by making the voltage HIGH
  }
}

void reconnect()
{
  while (!Mqttclient.connected())
  {
    Serial.print("Attempting MQTT connection...");
    if (Mqttclient.connect(name_device))
    {
      Serial.println("connected");
      Mqttclient.subscribe("servo/manual/E");
      Mqttclient.subscribe("servo/manual/A");
    }
    else
    {
      Serial.print("failed, rc=");
      Serial.print(Mqttclient.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

/*
***
***
***   MAIN
***
***
***
 */

void loop()
{
  //Cada 20 minutos debe hacer un get a sun position y mover los servos de forma correspondiente - Necesitamos fechas.
  //GET_SunPosition();

  //Cada minuto, debe comprobar su pruduccion. En caso de ser maxima moverse hasta que la misma baje.

  //Cada 10 tomas se hace una media y se publica.

  //Si su produccion es maxima debe comunicarse con el resto de placas, para saber si se alcanza el maximo del circuito

  //Si se alcanza el maximo del circuito, todas las placas deben moverse para bajar su produccion.

  //Orden de ejecucion :
  // 1 - Colocar servos en funcion de hora
  // 2 - Bucle 10 - minuto a minuto comprobamos produccion.
  // 3 - Post con media de produccion.
  // 4 - Bucle de 10 - minuto a minuto comprobamos produccion.
  // 5 - Post de produccion + Get con nueva hora.

  timeClient.update();
  if (!Mqttclient.connected())
  {
    reconnect();
  }
  Mqttclient.loop();

  long now = millis();
  if (now - lastMsg > 2000)
  {
    lastMsg = now;
    Serial.print("Publish message: ");
    Serial.println(msg);
    Mqttclient.publish("casa/despacho/temperatura", msg);
  }
  //Serial.println(getDate());

  //GET_tests();
  //POST_tests();
  delay(1000);
}

/*
***
***
***   OPERACIONES JSON 
***
***
***
 */

void deserializePosition(String responseJson, String servo) // Llega por MQTT
{
  if (responseJson != "")
  {
    StaticJsonDocument<200> doc;

    //char json[] =
    //    "{\"sensor\":\"gps\",\"time\":1351824120,\"data\":[48.756080,2.302038]}";

    // Deserialize the JSON document
    DeserializationError error = deserializeJson(doc, responseJson);

    // Test if parsing succeeds.
    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    // Fetch values.
    //
    // Most of the time, you can rely on the implicit casts.
    // In other case, you can do doc["time"].as<long>();
    char const *code = doc["code"];
    float position = doc["position"];

    if (strcmp(code, name_device) == 0)
    {
      if (servo == "A")
      {
        moveServos(position, 0.0);
      }
      else if (servo == "E")
      {
        moveServos(0.0, position);
      }
      else
      {
        Serial.println("Error en String servo, bad argument");
      }
    }

    //Caso manual A
    //moveServos(position,0);
    //Caso manual E
    //moveServos(0,position);

    // Print values.
    Serial.println(code);
    Serial.println(position);
  }
}

String serializeBoardProduction(int id_board, int servoPositionE, int servoPositionA, String date, float production)
{
  StaticJsonDocument<200> doc;
  doc["id_board"] = id_board;
  doc["servoPositionE"] = servoPositionE;
  doc["servoPositionA"] = servoPositionA;
  doc["date"] = date;
  doc["production"] = production;
  String output;
  serializeJson(doc, output);
  Serial.println(output);

  return output;
}

String serializeLog(int id_board, String date, String issue)
{
  StaticJsonDocument<200> doc;

  // StaticJsonObject allocates memory on the stack, it can be
  // replaced by DynamicJsonDocument which allocates in the heap.
  //
  // DynamicJsonDocument  doc(200);

  // Add values in the document
  //
  doc["id_board"] = id_board;
  doc["date"] = date;
  doc["issue"] = issue;

  // Add an array.
  //
  //JsonArray data = doc.createNestedArray("data");
  /*data.add(lat);
  data.add(lon);*/
  // Generate the minified JSON and send it to the Serial port.
  //
  String output;
  serializeJson(doc, output);
  // The above line prints:
  // {"sensor":"gps","time":1351824120,"data":[48.756080,2.302038]}

  // Start a new line
  Serial.println(output);

  // Generate the prettified JSON and send it to the Serial port.
  //
  //serializeJsonPretty(doc, output);
  // The above line prints:
  // {
  //   "sensor": "gps",
  //   "time": 1351824120,
  //   "data": [
  //     48.756080,
  //     2.302038
  //   ]
  // }
  return output;
}

void deserializeSunPosition(String responseJson)
{
  if (responseJson != "")
  {
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, responseJson);
    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    /*int id = doc["id"];
        int id_coordinates = doc["id_coordinates"];
        String date = doc["date"];*/
    float elevation = doc["elevation"];
    float azimut = doc["azimut"];

    moveServos(azimut, elevation);
  }
}

void deserializeBoard(String responseJson)
{
  if (responseJson != "")
  {
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, responseJson);
    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    /*int id = doc["id"];
        int id_coordinates = doc["id_coordinates"];
        String date = doc["date"];*/
    maxProduction = doc["maxPower"];
  }
}

/*
***
***
***   AUXULIARES
***
***
***
 */

String getDate()
{
  unsigned long epochTime = timeClient.getEpochTime();
  String formattedTime = timeClient.getFormattedTime();

  struct tm *ptm = gmtime((time_t *)&epochTime); //Get day, month and year from epoch time

  //Get a time structure
  int day = ptm->tm_mday;
  int month = ptm->tm_mon + 1;
  int year = ptm->tm_year + 1900;

  char dayFormat[50];
  sprintf(dayFormat, "%02d", day); //To keep a leading zero when needed

  char monthFormat[50];
  sprintf(monthFormat, "%02d", month); //To keep a leading zero when needed

  String fecha = String(dayFormat) + "-" + String(monthFormat) + "-" + String(year) + " " + String(formattedTime);
  return fecha;
}

void moveServos(float azimut, float elevation)
{
  boolean inverso = false;
  if (azimut != 0)
  {
    if (azimut > 180.0)
    {
      azimut = 360.0 - azimut;
      inverso = true;
    }
    myservoA.write(ceilf(azimut));
  }
  if (elevation != 0)
  {
    if (inverso)
    {
      elevation = 180.0 - elevation;
    }
    myservoE.write(ceilf(elevation));
  }
}

/*
***
***
***   DEPURACION
***
***
***
 */

void test_status(int statusCode)
{
  delay(test_delay);
  if (statusCode == 200 || statusCode == 201)
  {
    Serial.print("TEST RESULT: ok (");
    Serial.print(statusCode);
    Serial.println(")");
  }
  else
  {
    Serial.print("TEST RESULT: fail (");
    Serial.print(statusCode);
    Serial.println(")");
  }
}

void test_response()
{
  Serial.println("TEST RESULT: (response body = " + response + ")");
  response = "";
}

void describe(char *description)
{
  if (describe_tests)
    Serial.println(description);
}

void GET_tests()
{
  //describe("Test GET with path");
  test_status(Restclient.get("/api/boards", &response));
  test_response();

  //describe("Test GET with path and response");
  test_status(Restclient.get("/api/sunPosition/dateFilterCliente", &response));
  test_response();
}

void POST_tests()
{
  /*String post_bodyLog = serializeLog(1, "", "Test POST Log from ESP8266"); //id_board, date must be blank, issue
  describe("(LOG) Test POST with path and body and response");
  test_status(Restclient.post("/api/log", post_bodyLog.c_str(), &response));
  test_response();

  String post_bodyBoardProduction = serializeBoardProduction(1, 4, 3, "", 222222); //id_board, positionServoE, positionServoA, date must be blank, production
  describe("(BoardProduction)Test POST with path and body and response");
  test_status(Restclient.post("/api/boardProduction", post_bodyBoardProduction.c_str(), &response));
  test_response();*/
}

/*
***
***
***   API REST
***
***
***
 */

void GET_SunPosition()
{
  //describe("Test GET with path");
  test_status(Restclient.get("api/sunPosition/dateFilterClient/", &response));
  deserializeSunPosition(response);
  test_response();

  //describe("Test GET with path and response");
  //test_status(Restclient.get("/api/sunPosition/dateFilterCliente", &response));
  //test_response();
}

void GET_Board()
{
  //describe("Test GET with path");
  test_status(Restclient.get(strcat("/api/board/", id_device), &response));
  deserializeBoard(response);
  test_response();

  //describe("Test GET with path and response");
  //test_status(Restclient.get("/api/sunPosition/dateFilterCliente", &response));
  //test_response();
}

void POST_LOG()
{
  /*String post_bodyLog = serializeLog(1, "", "Test POST Log from ESP8266"); //id_board, date must be blank, issue
  describe("(LOG) Test POST with path and body and response");
  test_status(Restclient.post("/api/log", post_bodyLog.c_str(), &response));
  test_response();

  String post_bodyBoardProduction = serializeBoardProduction(1, 4, 3, "", 222222); //id_board, positionServoE, positionServoA, date must be blank, production
  describe("(BoardProduction)Test POST with path and body and response");
  test_status(Restclient.post("/api/boardProduction", post_bodyBoardProduction.c_str(), &response));
  test_response();*/
}

void POST_BoardProduction()
{
  /*String post_bodyLog = serializeLog(1, "", "Test POST Log from ESP8266"); //id_board, date must be blank, issue
  describe("(LOG) Test POST with path and body and response");
  test_status(Restclient.post("/api/log", post_bodyLog.c_str(), &response));
  test_response();

  String post_bodyBoardProduction = serializeBoardProduction(1, 4, 3, "", 222222); //id_board, positionServoE, positionServoA, date must be blank, production
  describe("(BoardProduction)Test POST with path and body and response");
  test_status(Restclient.post("/api/boardProduction", post_bodyBoardProduction.c_str(), &response));
  test_response();*/
}