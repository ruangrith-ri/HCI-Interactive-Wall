/*
 * Test On Arduino MEGA 2560
 * 
 * DISTANCE_MAX mm (120-3500)
 */

#include "lds.h"

lds_scan_t lds_scan;

void setup() {
  Serial.begin(230400);
  Serial2.begin(230400);

  ldsInit(&lds_scan);

  delay(100);
  Serial2.print("b");
  Serial.println("SEND COMMAND b");

  pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
  static uint32_t pre_time;

  if (Serial.available()) {
    Serial2.write(Serial.read());
  }

  while (Serial2.available() > 0) {
    if (ldsUpdate(&lds_scan, Serial2.read()) == true) {
      //Serial.println(Serial2.read());
    }
    digitalWrite(LED_BUILTIN, 1);
  }
  digitalWrite(LED_BUILTIN, 0);

  if (millis() - pre_time >= 50) {
    pre_time = millis();

    Serial.print(lds_scan.data[270].range);
    Serial.print(" ");
    Serial.print(lds_scan.data[270].intensity);
    Serial.print(" ");
    Serial.print(lds_scan.scan_time);
    Serial.println(" ");
  }
}
