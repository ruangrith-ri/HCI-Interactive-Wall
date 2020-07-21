#include <Artnet.h>

#include "lds.h"

#define FRAMERATE 40
#define FRAMERATE_INTERVAL 1000/FRAMERATE

lds_scan_t lds_scan;

unsigned long previousTime = 0;


/////////////////////////////////////////////////////////////////////////////Ethernet stuff

const IPAddress ip(192, 168, 1, 117);
uint8_t mac[] = {0x01, 0x23, 0x45, 0x67, 0x89, 0xAB};

Artnet artnet;

uint32_t universe1 = 0;
uint32_t universe2 = 1;

const uint16_t size = 360;

uint8_t data1[size];
uint8_t data2[size];

uint8_t value = 0;

/////////////////////////////////////////////////////////////////////////////


void setup() {
  Ethernet.begin(mac, ip);
  artnet.begin("192.168.1.255", 6454, 6454);

  Serial.begin(230400);
  Serial2.begin(230400);

  ldsInit(&lds_scan);

  Serial2.print("b");
  Serial.println("LIDAR On");

  pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
  if (Serial.available()) {
    Serial2.write(Serial.read());
  }

  while (Serial2.available() > 0) {
    if (ldsUpdate(&lds_scan, Serial2.read()) == true) {
      mapData();
      sendArtnet();
      digitalWrite(LED_BUILTIN, 1);
    }
  }
  digitalWrite(LED_BUILTIN, 0);

  static uint32_t pre_time;
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

void mapData() {
  for (int i = 0; i < 180; i++) {
    data1[i * 2] = lds_scan.data[i].range;
    data1[i * 2 + 1] = floor(lds_scan.data[i].range / 256.0);
  }

  for (int i = 180; i < 360; i++) {
    data2[(i - 180) * 2] = lds_scan.data[i].range;
    data2[(i - 180) * 2 + 1] = floor(lds_scan.data[i].range / 256.0);
  }
}

void sendArtnet() {
  artnet.send(universe1, data1, size);
  artnet.send(universe2, data2, size);
}
