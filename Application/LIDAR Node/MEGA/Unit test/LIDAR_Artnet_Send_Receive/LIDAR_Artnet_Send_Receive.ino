#include <Artnet.h>

#include "lds.h"

#define FRAMERATE 5
#define FRAMERATE_INTERVAL 1000/FRAMERATE

#define TIMEOUT 3000

lds_scan_t lds_scan;


uint32_t lastReciveDataTime = 0;

///////////////////////////////////////////////////////////////////////////// ethernet stuff

const IPAddress ip(192, 168, 1, 117);
uint8_t mac[] = {0x01, 0x23, 0x45, 0x67, 0x89, 0xAB};

Artnet artnet;

uint32_t universe0 = 0;
uint32_t universe1 = 1;
uint32_t universe2 = 2;

const uint16_t size = 360;

uint8_t data1[size];
uint8_t data2[size];

///////////////////////////////////////////////////////////////////////////// command lds

bool ldsOn = false;
bool lastCommand = false;

void callback(uint8_t* data, uint16_t size) {
  bool command = data[0] > 127;

  if (lastCommand < command) {
    Serial2.write("b");
    ldsOn = true;
    lastReciveDataTime = millis();
  } else if (lastCommand > command) {
    Serial2.write("e");
    ldsOn = false;

    clearData();
    sendData();
  }

  lastCommand = command;
}

/////////////////////////////////////////////////////////////////////////////


void setup() {
  Ethernet.begin(mac, ip);

  artnet.begin("192.168.1.255", 6454, 6454);
  artnet.subscribe(universe0, callback);

  Serial.begin(115200);
  Serial2.begin(230400);

  ldsInit(&lds_scan);

  Serial2.write("b");
  Serial.println("LIDAR On");
}


void loop() {
  reciveData();

  if (ldsOn && millis() - lastReciveDataTime > TIMEOUT) {
    lastReciveDataTime = millis();
    Serial2.write("b");
    Serial.println("Timeout");
  }

  while (Serial2.available() > 0) {
    lastReciveDataTime = millis();

    if (ldsUpdate(&lds_scan, Serial2.read()) == true) {
      mapData();
      sendData();
    }
  }
}


///////////////////////////////////////////////////////////////////////////// send data

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

void clearData() {
  memset(data1, 0, size);
  memset(data2, 0, size);
}

void sendData() {
  artnet.send(universe1, data1, size);
  artnet.send(universe2, data2, size);
}

void reciveData() {
  static uint32_t previousTime;

  if (millis() - previousTime >= FRAMERATE_INTERVAL) {
    previousTime = millis();
    artnet.parse(); // execute callback
  }
}
