EthernetUDP UDP;

byte destination_Ip[] = {192, 168, 1, 255};
unsigned int localPort = 6454;

const int number_of_channels = 720;

////////////////////////////////////////////////////////////////////////////////////////////////// ART-NET variables

char packetBuffer[UDP_TX_PACKET_MAX_SIZE]; //buffer to hold incoming packet,
char ArtNetHead[8] = "Art-Net";
const int art_net_header_size = 17;

short OpOutput = 0x5000 ; //output
byte buffer_dmx[number_of_channels]; //buffer used for DMX data

byte  ArtDmxBuffer[(art_net_header_size + number_of_channels) + 8 + 1];

////////////////////////////////////////////////////////////////////////////////////////////////// Main Call

void udp_begin() {
  UDP.begin(localPort);
}

void udp_send() {
  UDP.beginPacket(destination_Ip, localPort);
  UDP.write(ArtDmxBuffer, (art_net_header_size + number_of_channels + 1));
  UDP.endPacket();

  UDP.flush();
}

////////////////////////////////////////////////////////////////////////////////////////////////// RX

void recive_arnet_packet() {
  int packetSize = UDP.parsePacket();

  if (packetSize) {
    UDP.read(packetBuffer, UDP_TX_PACKET_MAX_SIZE);

    //Finish reading this packet
    UDP.flush();

    //Pour Tests
    bool match_artnet = true; //valeur de stockage

    int universe = (byte(packetBuffer[15]) << 8) | byte(packetBuffer[14]);
    if (universe == universe0) {
      for (int i = 0; i < 7; i++) {
        if (char(packetBuffer[i]) != ArtNetHead[i]) {
          match_artnet = false;
          break;
        }
      }
    } else {
      match_artnet = false;
    }

    if (match_artnet) {

      //Universe match
      Serial.print("Universe : ");
      Serial.print(universe, DEC);

      //Data Length
      Serial.print("\t Data Length : ");
      Serial.println((byte(packetBuffer[16]) << 8) | byte(packetBuffer[17]), DEC);

      //Data
      int d = (byte)packetBuffer[18];
      Serial.println(d, DEC);
      Serial.println();

      lds_command(d); //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Command
    }
  }
}

////////////////////////////////////////////////////////////////////////////////////////////////// TX

void construct_arnet_packet() {

  //Pour Tests
  for (int i = 0; i < 7; i++) {
    ArtDmxBuffer[i] = ArtNetHead[i];
  }

  //OpCode
  ArtDmxBuffer[8] = OpOutput;
  ArtDmxBuffer[9] = OpOutput >> 8;

  //Protocol
  ArtDmxBuffer[10] = 0;
  ArtDmxBuffer[11] = 14;

  //Sequence
  ArtDmxBuffer[12] = 0;

  //Physical
  ArtDmxBuffer[13] = 0;

  //Universe
  ArtDmxBuffer[14] = universe1;
  ArtDmxBuffer[15] = universe1 >> 8;

  //Data Length
  ArtDmxBuffer[16] = number_of_channels >> 8;
  ArtDmxBuffer[17] = number_of_channels;

  for (int i = 0; i < 360; i++) {
    ArtDmxBuffer[(i * 2) + art_net_header_size + 1] = lds_scan.data[i].range;
    ArtDmxBuffer[(i * 2 + 1) + art_net_header_size + 1] = lds_scan.data[i].range >> 8;
  }
}


void zero_arnet_packet() {
  //Pour Tests
  for (int i = 0; i < 7; i++) {
    ArtDmxBuffer[i] = ArtNetHead[i];
  }

  //OpCode
  ArtDmxBuffer[8] = OpOutput;
  ArtDmxBuffer[9] = OpOutput >> 8;

  //Protocol
  ArtDmxBuffer[10] = 0;
  ArtDmxBuffer[11] = 14;

  //Sequence
  ArtDmxBuffer[12] = 0;

  //Physical
  ArtDmxBuffer[13] = 0;

  //Universe
  ArtDmxBuffer[14] = universe1;
  ArtDmxBuffer[15] = universe1 >> 8;

  //Data Length
  ArtDmxBuffer[16] = number_of_channels >> 8;
  ArtDmxBuffer[17] = number_of_channels;

  for (int i = 0; i < 720; i++) {
    ArtDmxBuffer[(i) + art_net_header_size + 1] = 0;
  }
}

//////////////////////////////////////////////////////////////////////////////////////////////////
