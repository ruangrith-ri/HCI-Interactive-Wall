#include <Wire.h>
#include "Adafruit_MPR121.h"

Adafruit_MPR121 cap = Adafruit_MPR121();

void setup() {
  Serial.begin(115200);

  while (!Serial) {
    delay(10);
  }

  if (!cap.begin(0x5A)) {
    Serial.println("MPR121 not found, check wiring?");
    while (1);
  }

  Serial.println("MPR121 found!");
}

void loop() {
  for (uint8_t i = 0; i < 12; i++) {
    Serial.print(getTouch(i));
    Serial.print(" ");
  }
  Serial.println();

  delay(0);
}

bool getTouch(uint8_t electrode) {
  return cap.touched() & 1 << (electrode);
}
