#include <Artnet.h>

#define FRAMERATE 40
#define FRAMERATE_INTERVAL 1000/FRAMERATE

unsigned long previousTime = 0;

// Ethernet stuff
const IPAddress ip(192, 168, 1, 117);
uint8_t mac[] = {0x01, 0x23, 0x45, 0x67, 0x89, 0xAB};

Artnet artnet;
uint32_t universe = 0;

const uint16_t size = 512;
uint8_t data[size];
uint8_t value = 0;

uint8_t localPort = 6454;

void setup()
{
  Ethernet.begin(mac, ip);
  artnet.begin("192.168.1.255", 6454, 6454);
}

void loop() {
  value = millis() % 256;
  memset(data, value, size);

  sendArtnetInterval(FRAMERATE_INTERVAL);
}

void sendArtnetInterval(int frameTime) {
  unsigned long currentTime = millis();

  if (currentTime - previousTime >= frameTime) {
    artnet.send(0, data, size);
    artnet.send(1, data, size);
    
    previousTime = currentTime;
  }
}
