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
#include <Servo.h>

//Network Time Protocol (NTP) es un protocolo de Internet para sincronizar los relojes de los sistemas informáticos
//a través del enrutamiento de paquetes en redes con latencia variable. NTP utiliza UDP como su capa de transporte,
//usando el puerto 123. Está diseñado para resistir los efectos de la latencia variable.
#include <NTPClient.h>
#include <WiFiUdp.h>

#include "ArduinoJson.h"
#include "RestClient.h"
#include "config.h"

#define LENGTH_AVERAGE_PRODUCTION 3
#define TIME_BETWEEN_READS 10000

#define MOVE_MANUAL 0
#define MOVE_AUTO 1
#define MOVE_AUTO_OVERLOAD 2
#define ANGLE_MOD 5 // Ángulo a restar al movimiento del servo si Sobrecarga

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
void deserializePosition(String);

void deserializeProduction(String);

void GET_MaxPowerNumberBoards(void);

void MQTT_Production(float media);

//Para leer JSON de SunPosition
void deserializeSunPosition(String);

//Para leer JSON de Board
void deserializeBoard(String);

//Para mover los servos
void moveServos(float, float);

//Para leer la producción
float getProduction();

//GET a SunPosition
void GET_SunPosition();
//GET a Board
void GET_Board();

//POST a Log
void POST_LOG(int);

//POST a BoardProduction
void POST_BoardProduction(float);

//Tests response
void test_response();
void test_status(int);

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

//ids
int id_board = 1;
int id_coordinates;

//Productions
float MAXPOWER_board = 10; //0.0;
float MAXPOWER_all = 10;   //0.0;
float MAX_POWER_WHOLE_SYSTEM = 100;
int overload = 0;

//Array de floats para medias de producciones
float currentProductionArray[LENGTH_AVERAGE_PRODUCTION];
int indexProductionArray;
int indexPOSTSunPosition = 0;

//Para los timers
long lastMsg = 0;
//NOSE SI LO VAMOS A USAR PENDIENTE DE BORRAR
char msg[50];

int test_delay = 1000; //so we don't spam the API
boolean describe_tests = true;

//Para almacenar el Code de la placa
char const *code;
//Para almacenar la posicion de la placa
int position;
//Numero de placas, modificar cuando tengamos GET correspondiente
int numBoards = 2;

//Para guardar el resultado de los GET
String response;

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
  Serial.begin(115200);

  pinMode(D2, OUTPUT); //Pin servo Azimut
  pinMode(D3, OUTPUT); //Pin servo Elevation
  pinMode(A0, INPUT);  //Pin entrada Placas solares (Analógico para que funcione como Polímetro)

  setup_wifi(); //Llamamos la funcion para establecer el wifi

  Mqttclient.setServer(mqtt_server, 1883); //Mqtt client con el puerto y la funcion callback
  Mqttclient.setCallback(callback);

  myservoA.attach(D2); //Attach servo Azimut al pin D2
  myservoE.attach(D3); //Attach servo Elevation al pin D3

  timeClient.begin(); //Time

  GET_Board();
  GET_MaxPowerNumberBoards();
  GET_SunPosition();
  indexProductionArray = 0;
}

void setup_wifi()
{
  delay(10);
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

  if ((String)topic == "/servo/manualPosition")
  {
    deserializePosition(JsonObject);
    POST_LOG(MOVE_MANUAL); //Post manual
  }

  else if ((String)topic == "/board/production")
  {
    deserializeProduction(JsonObject);
    //POST_LOG(MOVE_AUTO_OVERLOAD); //Post Auto por Overload
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
      Mqttclient.subscribe("/servo/manualPosition");
      Mqttclient.subscribe("/board/production");
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

  timeClient.update();
  if (!Mqttclient.connected())
  {
    reconnect();
  }
  Mqttclient.loop();

  long now = millis();
  if (now - lastMsg > TIME_BETWEEN_READS)
  {
    lastMsg = now;

    currentProductionArray[indexProductionArray] = getProduction();                //Comprobación de la Produccion
    if (currentProductionArray[indexProductionArray] > MAXPOWER_board / numBoards) // Activa overload y publica en el topic
    {
      overload = 1;
      MQTT_Production(currentProductionArray[indexProductionArray]);
    }
    while (overload) //Si overload se mueven ambos servos
    {
      Serial.println("OVERLOAD");
      myservoA.write(myservoA.read() - ANGLE_MOD);
      myservoE.write(myservoE.read() - ANGLE_MOD);
      if (getProduction() < MAXPOWER_board / numBoards) //Mientras se mueven los servos se busca quitar el overload, cuando lo consigue lo pone a 0 y publica en el topic
      {
        overload = 0;
        MQTT_Production(currentProductionArray[indexProductionArray]);
        POST_LOG(MOVE_AUTO_OVERLOAD); //Post Auto por Overload
      }
    }
    indexProductionArray++;                                //Aumenta iterador del Array de medias
    if (indexProductionArray >= LENGTH_AVERAGE_PRODUCTION) //Se calcula la media si iterador mayor que LENGTH_AVERAGE_PRODUCTION
    {
      float mediaProduction = 0.0;
      for (int i = 0; i < LENGTH_AVERAGE_PRODUCTION; i++)
      {
        mediaProduction += currentProductionArray[i];
      }
      mediaProduction = mediaProduction / (LENGTH_AVERAGE_PRODUCTION * 1.0);
      POST_BoardProduction(mediaProduction);
      indexProductionArray = 0;
      indexPOSTSunPosition++;
      if (indexPOSTSunPosition >= 2)
      {
        GET_SunPosition();
        indexPOSTSunPosition = 0;
        POST_LOG(MOVE_AUTO);
      }
    }
  }

  //Cada 20 minutos debe hacer un get a sun position y mover los servos de forma correspondiente - Necesitamos fechas.
  //GET_SunPosition(); Se hace en el SETUP

  //Cada minuto, debe comprobar su produccion. En caso de ser maxima moverse hasta que la misma baje.

  //Cada 10 tomas se hace una media y se publica.

  //Si su produccion es maxima debe comunicarse con el resto de placas, para saber si se alcanza el maximo del circuito

  //Si se alcanza el maximo del circuito, todas las placas deben moverse para bajar su produccion.

  //Orden de ejecucion :
  // 1 - Colocar servos en funcion de hora (Esto se hace en el Setup)
  // 2 - Bucle 10 - minuto a minuto comprobamos produccion. (Esto se hace en el loop)
  // 3 - Post con media de produccion. (Esto se hace en el loop)
  // 4 - Bucle de 10 - minuto a minuto comprobamos produccion. (Esto se hace en el loop)
  // 5 - Post de produccion + Get con nueva hora. (Esto se hace en el loop)

  delay(1000);
  //}
}

/*
***
***
***   OPERACIONES JSON 
***
***
***
 */

void deserializePosition(String responseJson) // Llega por MQTT
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
    char const *code = doc["code"];
    float elevation = doc["elevation"];
    float azimut = doc["azimut"];
    if (strcmp(code, name_device) == 0)
    {
      moveServos(azimut, elevation);
    }
    /*
    Serial.println(code);
    Serial.print("Elevation: ");
    Serial.println(elevation);
    Serial.print("Azimut: ");
    Serial.println(azimut);
    */
  }
}

void deserializeProduction(String responseJson) // Llega por MQTT
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
    //char const *code = doc["code"];
    //float production = doc["production"];
    overload = doc["overload"];
  }
}

String serializeBoardProduction(int id_board, int servoPositionE, int servoPositionA, String date, float production)
{
  StaticJsonDocument<200> doc;
  doc["id_board"] = id_board;
  doc["positionServoE"] = servoPositionE;
  doc["positionServoA"] = servoPositionA;
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
  doc["id_board"] = id_board;
  doc["date"] = date;
  doc["issue"] = issue;
  String output;
  serializeJson(doc, output);
  Serial.println(output);
  return output;
}

String serializeProduction(float production)
{
  StaticJsonDocument<200> doc;
  doc["code"] = name_device;
  doc["production"] = production;
  doc["overload"] = overload;
  String output;
  serializeJson(doc, output);
  Serial.println(output);
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
    float elevation = doc[0]["elevation"];
    float azimut = doc[0]["azimut"];
    Serial.print("Elevation desSunPosition: ");
    Serial.println(elevation);
    Serial.print("Azimut desSunPosition: ");
    Serial.println(azimut);
    moveServos(azimut, elevation);
  }
}

void deserializeBoard(String responseJson)
{
  if (responseJson != "")
  {
    StaticJsonDocument<500> doc;
    DeserializationError error = deserializeJson(doc, responseJson);
    if (error)
    {
      Serial.print(F("deserializeBoard deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    id_board = doc[0]["id"];
    id_coordinates = doc[0]["coordinate"]["id"];
    //String date = doc["date"];
    MAXPOWER_board = doc[0]["maxPower"];
  }
}

/*
***
***
***   AUXILIARES
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
  //boolean inverso = false;
  if (azimut >= 0 && azimut <= 180)
  {
    myservoA.write(180 - azimut);
    myservoE.write(elevation);
  }
  if (azimut > 180 && azimut <= 360)
  {
    myservoA.write(360 - azimut);
    myservoE.write(180 - elevation);
  }

  /*
  if (azimut >= 0)
  {
    if (azimut > 180.0)
    {
      azimut = 360.0 - azimut;
      inverso = true;
    }
    else
    {
      azimut -= 180;
      inverso = false;
    }
    myservoA.write(ceilf(azimut));
    Serial.print("Azimut: ");
    Serial.println(ceilf(azimut));
  }
  if (elevation >= 0)
  {
    if (inverso)
    {
      elevation -= 180;
      myservoE.write(ceilf(elevation));
    }
    myservoE.write(ceilf(elevation));
    Serial.print("Elevation: ");
    Serial.println(ceilf(elevation));
  }*/
}

float getProduction()
{
  float analog = 0.0;
  float analog3v3 = 0.0;
  float v33 = 3.3;
  float mil23 = 1023.0;

  analog = analogRead(A0);
  analog3v3 = analog / mil23;
  analog3v3 = analog3v3 * v33;
  return analog3v3;
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
  String path = "/api/sunPosition/dateFilterClient/";
  path += id_coordinates;
  test_status(Restclient.get(path.c_str(), &response));
  deserializeSunPosition(response);
  test_response();
}

void GET_Board()
{
  String path = "/api/board/";
  path += name_device;
  Serial.println(path.c_str());
  test_status(Restclient.get(path.c_str(), &response));
  deserializeBoard(response);
  test_response();
}

void GET_MaxPowerNumberBoards()
{
  String path = "/api/board/info/";
  test_status(Restclient.get(path.c_str(), &response));
  //deserializeMaxPower(response);
  test_response();
}

void POST_LOG(int move)
{
  if (move == 0)
  {
    String post_bodyLog = serializeLog(id_board, "", "Movimiento manual de la placa."); //id_board, date must be blank, issue
    test_status(Restclient.post("/api/log", post_bodyLog.c_str(), &response));
    test_response();
  }
  if (move == 1)
  {
    String post_bodyLog = serializeLog(id_board, "", "Movimiento automático de la placa."); //id_board, date must be blank, issue
    test_status(Restclient.post("/api/log", post_bodyLog.c_str(), &response));
    test_response();
  }
  if (move == 2)
  {
    String post_bodyLog = serializeLog(id_board, "", "Movimiento automático de la placa por Sobrecarga."); //id_board, date must be blank, issue
    test_status(Restclient.post("/api/log", post_bodyLog.c_str(), &response));
    test_response();
  }
}

void POST_BoardProduction(float media)
{
  String post_bodyBoardProduction = serializeBoardProduction(id_board, myservoE.read(), myservoA.read(), getDate(), media); //id_board, positionServoE, positionServoA, date must be blank, production
  test_status(Restclient.post("/api/boardProduction", post_bodyBoardProduction.c_str(), &response));
  test_response();
}

void MQTT_Production(float media)
{
  String output = serializeProduction(media);
  Serial.print("Publish message: ");
  Serial.println(output);
  Mqttclient.publish("/board/production", output.c_str());
}