#include <Artnet.h>
#include <Adafruit_MPR121.h>

Adafruit_MPR121 MPR121 = Adafruit_MPR121();

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

  if (! MPR121.begin(0x5A)) {
    //MPR121 not found, check wiring?
    while (1);
  }
}

void loop() {
  mapTouchData();

  static uint32_t previousTime;
  if (millis() - previousTime >= 1000 / 40) {
    previousTime = millis();
    artnet.send(universe, data, size);
  }
}

/////////////////////////////////////////////////////////////////////////////


void mapTouchData() {
  for (int i = 0 ; i < size ; i++) {
    data[i] = getTouchFilter(i) ? 255 : 0;
  }
}

bool getTouchFilter(uint8_t electrode) {
  return MPR121.filteredData(electrode) > 150 ? false : true;
}
