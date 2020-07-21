#include <Artnet.h>

// Ethernet stuff
const IPAddress ip(192, 168, 1, 201);
uint8_t mac[] = {0x01, 0x23, 0x45, 0x67, 0x89, 0xAB};

Artnet artnet;
uint32_t universe1 = 0;
uint32_t universe2 = 1;

void callback(uint8_t* data, uint16_t size){
  Serial.print("artnet data (universe : ");
  Serial.print(universe1);
  Serial.println(") = ");
  for (size_t i = 0; i < size; ++i){
    Serial.print(data[i]); Serial.print(",");
  }
  Serial.println();
}

void setup(){
  Serial.begin(115200);
  Ethernet.begin(mac, ip);
  artnet.begin(8080);

  artnet.subscribe(universe1, callback);
}

void loop(){
  artnet.parse(); // check if artnet packet has come and execute callback
}
