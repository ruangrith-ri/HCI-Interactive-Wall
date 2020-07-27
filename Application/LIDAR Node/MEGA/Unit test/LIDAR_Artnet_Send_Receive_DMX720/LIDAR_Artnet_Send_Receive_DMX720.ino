#include <Ethernet.h>

#include "lds.h"

#define FRAMERATE 5
#define FRAMERATE_INTERVAL 1000/FRAMERATE

#define TIMEOUT 5000

lds_scan_t lds_scan;

uint32_t lastReciveDataTime = 0;

///////////////////////////////////////////////////////////////////////////// ethernet stuff

const IPAddress ip(192, 168, 1, 117);
uint8_t mac[] = {0x01, 0x23, 0x45, 0x67, 0x89, 0xAB};

uint32_t universe0 = 0;
uint32_t universe1 = 1;

///////////////////////////////////////////////////////////////////////////// command lds

bool ldsOn = false;
bool lastCommand = false;

void lds_command(int d) {
  bool command = d > 127;

  if (lastCommand < command) {
    Serial2.write("b");
    ldsOn = true;
    lastReciveDataTime = millis();
      Serial.println("LIDAR On2");
  } else if (lastCommand > command) {
    Serial2.write("e");
    ldsOn = false;
    Serial.println("LIDAR Off2");
    
    clearData();
    sendData();
  }

  lastCommand = command;
}

/////////////////////////////////////////////////////////////////////////////


void setup() {
  Ethernet.begin(mac, ip);
  
  udp_begin();

  //Serial.begin(115200);
  Serial2.begin(230400);

  ldsInit(&lds_scan);

  Serial2.write("e");
  Serial.println("LIDAR Off");
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
  construct_arnet_packet();
}

void clearData() {
  zero_arnet_packet();
}

void sendData() {
  udp_send();
}

void reciveData() {
  static uint32_t previousTime;

  if (millis() - previousTime >= FRAMERATE_INTERVAL) {
    previousTime = millis();
    recive_arnet_packet();
  }
}
