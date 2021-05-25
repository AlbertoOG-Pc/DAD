#include "../headerclass/headerboard.h"

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
