#include <Ethernet.h>

////////////////////////////////////////////////////////////////////////////////////////////////

EthernetUDP Udp;

byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
IPAddress ip(192, 168, 1, 177);
unsigned int localPort = 6454;

char packetBuffer[UDP_TX_PACKET_MAX_SIZE]; //buffer to hold incoming packet,
char ArtNetHead[8] = "Art-Net";

////////////////////////////////////////////////////////////////////////////////////////////////

void setup() {
  Serial.begin(115200);
  Serial.print("Test");
  
  Ethernet.begin(mac, ip);
  Udp.begin(localPort);
}

void loop() {

  int packetSize = Udp.parsePacket();
  if (packetSize) {
    Serial.print("Received packet of size ");
    Serial.println(packetSize);

    //IP
    IPAddress ipAddress = Udp.remoteIP();

    Serial.print("From IP ");
    Serial.print(
      String(ipAddress[0]) + "." + 
      String(ipAddress[1]) + "." + 
      String(ipAddress[2]) + "." + 
      String(ipAddress[3])
    );

    //Port
    Serial.print(", port ");
    Serial.println(Udp.remotePort());

    Udp.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);

    Serial.print("Contents: ");
    Serial.println(packetBuffer);

    //Pour Tests
    bool match_artnet = true; //valeur de stockage
    for (int i = 0; i < 7; i++) {
      if (char(packetBuffer[i]) != ArtNetHead[i]) {
        match_artnet = false;
        break;
      }
    }

    if (match_artnet) {
      //OpCode
      Serial.print("OpCode : ");
      Serial.print((packetBuffer[9] << 8) | packetBuffer[8], HEX);

      //Protocol
      Serial.print("\t Protocol : ");
      Serial.print((packetBuffer[10] << 8) | packetBuffer[11], DEC);

      //Sequence
      Serial.print("\t Sequence : ");
      Serial.print((byte)packetBuffer[12], DEC);

      //Physical
      Serial.print("\t Physical : ");
      Serial.println(packetBuffer[13], DEC);

      //Universe
      Serial.print("Universe : ");
      Serial.print((byte(packetBuffer[15]) << 8) | byte(packetBuffer[14]), DEC);

      //Data Length
      Serial.print("\t Data Length : ");
      Serial.println((byte(packetBuffer[16]) << 8) | byte(packetBuffer[17]), DEC);

      //Data
      Serial.println((byte)packetBuffer[18], DEC);
      Serial.println();
    }
  }
}
