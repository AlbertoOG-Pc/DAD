#include "RestClient.h"
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include "config.h"

void setup_wifi();
void callback(char *, byte *, unsigned int);

WiFiClient espClient;
PubSubClient Mqttclient(espClient);
long lastMsg = 0;
char msg[50];

RestClient Restclient = RestClient(mqtt_server, 8089);

int test_delay = 1000; //so we don't spam the API
boolean describe_tests = true;

void setup()
{
  pinMode(LED_BUILTIN, OUTPUT);
  Serial.begin(115200);
  setup_wifi();
  Mqttclient.setServer(mqtt_server, 1883);
  Mqttclient.setCallback(callback);
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
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  for (unsigned int i = 0; i < length; i++)
  {
    Serial.print((char)payload[i]);
  }
  Serial.println();

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
    if (Mqttclient.connect("ESP1269813124G"))
    {
      Serial.println("connected");
      Mqttclient.publish("casa/despacho/temperatura", "Enviando el primer mensaje");
      Mqttclient.subscribe("casa/despacho/luz");
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

String response;

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
  test_status(Restclient.get("/api/log", &response));
  test_response();

  /*describe("Test GET with path");
  test_status(Restclient.get("/api/sensors/123", &response));
  test_response();*/
}



void loop()
{

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
  GET_tests();
}