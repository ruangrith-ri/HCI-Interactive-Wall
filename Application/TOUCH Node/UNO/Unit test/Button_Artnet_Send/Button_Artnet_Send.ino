#include <Artnet.h>

/////////////////////////////////////////////////////////////////////////////Ethernet stuff

const IPAddress ip(192, 168, 1, 116);
uint8_t mac[] = {0x01, 0x23, 0x45, 0x67, 0x89, 0xAB};

Artnet artnet;

uint32_t universe = 3;
const uint16_t size = 12;
uint8_t data[size];

/////////////////////////////////////////////////////////////////////////////

void setup() {
  Ethernet.begin(mac, ip);
  artnet.begin("192.168.1.255", 6454);

  pinMode(2, INPUT_PULLUP);
}

void loop() {
  readTouch();
  
  artnet.set(universe, data, size);
  artnet.streaming();
}

/////////////////////////////////////////////////////////////////////////////

void readTouch() {
  for (int i = 0 ; i < size ; i++) {
    data[i] = digitalRead(2) ? 0 : 255;
  }
}
