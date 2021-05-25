#include "ArduinoJson.h"

String serializeBoardProduction(int, int, int, String, float);
String serializeLog(int, String, String);
void deserializePosition(String, const char *, int);
void deserializeSunPosition(String, int *, int *);
