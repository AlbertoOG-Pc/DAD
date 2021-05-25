#include "../headerclass/headerboard.h"

void deserializeSunPosition(String responseJson, int *elevation, int *azimut)
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
        elevation = doc["elevation"];
        azimut = doc["azimut"];
    }
}