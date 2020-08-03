#include <Wire.h>
#include "Adafruit_MPR121.h"

Adafruit_MPR121 cap = Adafruit_MPR121();

void setup() {
  Serial.begin(115200);

  while (!Serial) {
    delay(10);
  }

 // Serial.println("Adafruit MPR121 Capacitive Touch sensor test");

  if (!cap.begin(0x5A)) {
    //Serial.println("MPR121 not found, check wiring?");
    while (1);
  }

  //Serial.println("MPR121 found!");
}

void loop() {
  //Serial.print("Filt: \t");
  for (uint8_t i = 0; i < 12; i++) {
    Serial.print(/*getTouchFilter(i)*/cap.filteredData(i));
    Serial.print(" ");
  }
  Serial.println();

  delay(10);
}

bool getTouchFilter(uint8_t electrode) {
  return cap.filteredData(electrode) > 240 ? false : true;
}
