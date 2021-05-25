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
#include "headerclass/headerboard.h"

void setup_wifi();
void callback(char *, byte *, unsigned int);

WiFiClient espClient;
PubSubClient Mqttclient(espClient);
RestClient Restclient = RestClient(mqtt_server, 8089);

Servo myservoA;
Servo myservoE;

//Time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org"); //CSV must be UCT (+0) No DST

long lastMsg = 0;
char msg[50];
int test_delay = 1000; //so we don't spam the API
boolean describe_tests = true;

const char *code;
int position;
String response;

void setup()
{
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(D2, OUTPUT);
  pinMode(D3, OUTPUT);
  Serial.begin(115200);
  setup_wifi();
  Mqttclient.setServer(mqtt_server, 1883);
  Mqttclient.setCallback(callback);
  myservoA.attach(D2);
  myservoE.attach(D3);
  //Time
  timeClient.begin();
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

void callback(char *topic, byte *payload, unsigned int length)
{
  String JsonObject;
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
    deserializePosition(JsonObject, code, position);

    if (strcmp(code, name_device) == 0)
    {
      //Mueve servo
      myservoA.write(position);
    }
  }
  else if ((String)topic == "servo/manual/E")
  {
    deserializePosition(JsonObject, code, position);

    if (strcmp(code, name_device) == 0)
    {
      myservoE.write(position);
    }
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
      //Mqttclient.publish("casa/despacho/temperatura", "Enviando el primer mensaje");
      //Mqttclient.subscribe("casa/despacho/luz");
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
  describe("Test GET with path");
  test_status(Restclient.get("/api/boards", &response));
  test_response();

  describe("Test GET with path and response");
  test_status(Restclient.get("/api/sunPosition/dateFilterCliente", &response));
  test_response();
}

void POST_tests()
{
  String post_bodyLog = serializeLog(1, "" /*getDate()*/, "Test POST Log from ESP8266"); //id_board, date must be blank, issue
  describe("(LOG) Test POST with path and body and response");
  test_status(Restclient.post("/api/log", post_bodyLog.c_str(), &response));
  test_response();

  String post_bodyBoardProduction = serializeBoardProduction(1, 4, 3, "" /*getDate()*/, 222222); //id_board, positionServoE, positionServoA, date must be blank, production
  describe("(BoardProduction)Test POST with path and body and response");
  test_status(Restclient.post("/api/boardProduction", post_bodyBoardProduction.c_str(), &response));
  test_response();
}

void loop()
{
  //Cada 20 minutos debe hacer un get a sun position y mover los servos de forma correspondiente - Necesitamos fechas.

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