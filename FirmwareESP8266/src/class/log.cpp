#include "../headerclass/headerboard.h"

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