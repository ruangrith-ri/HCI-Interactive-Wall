import ch.bildspur.artnet.*;

PFont nexaL;
PFont nexaLL;
PFont nexaB;
PFont nexaBL;

ArtNetClient artnet;
byte[] dmxData = new byte[512];

int lastCommand = 0;
int c;

boolean onLDS = false;

long timeOnLDS = 0;

float timer = 0;

/////////////////////////////////////////////////////////////////////////////////////////

void setup() {
  size(1000, 1000);
  frameRate(40);
  textAlign(LEFT, CENTER);
  ellipseMode(CENTER);
  textSize(20);

  artnet = new ArtNetClient();
  artnet.start();

  nexaL = createFont("font/NexaDemo-Light.otf", 11);
  nexaLL = createFont("font/NexaDemo-Light.otf", 20);
  nexaB = createFont("font/NexaDemo-Bold.otf", 11);
  nexaBL = createFont("font/NexaDemo-Bold.otf", 20);

  textFont(nexaBL);
}

void draw() {
  background(0);

  byte[] data = artnet.readDmxData(0, 3);
  int command = byteToShort(data[0]);

  if (command > lastCommand) {
    onLDS = true;
    timeOnLDS = millis();
  }

  if (millis() - timeOnLDS >= 30000) {
    onLDS = false;
  }

  if (onLDS) {
    c = 255;

    timer = (30 - ((millis() - timeOnLDS)/1000.0));
    textFont(nexaLL);
    text("Timer : " + int(timer), 25, 75);
    drawPolar();
  } else {
    c = 0;
  }

  lastCommand = command;

  dmxData[0] = (byte) c;

  artnet.broadcastDmx(0, 0, dmxData);

  textFont(nexaBL);
  text("Command form TOUCH Node: " + command, 25, 25);
  text("Command to LIDAR Node: " + c, 25, 50);
}

/////////////////////////////////////////////////////////////////////////////////////////

short byteToShort(byte dmxByte) {
  return (short) (dmxByte & 0xFF);
}

// int c = (int)map(mouseX, 0, width, 0, 255);

void drawPolar() {
  byte[] data1 = artnet.readDmxData(0, 1);
  byte[] data2 = artnet.readDmxData(0, 2);

  push();

  translate(500, 500);

  fill(0, 0, 0, 0);

  for (int i = 50; i <=700; i += 50) {
    stroke(   constrain((((millis() - timeOnLDS)/10.0)-i/5.0), 0, 128)   );
    circle(0, 0, i);
  }

  stroke(0, 255, 0);
  strokeWeight(1);

  for (int i = 0; i < 180; i++) {
    pushMatrix();

    int value = byteToShort(data1[i * 2]);
    int valueCarry = byteToShort(data1[i * 2 + 1]) * 256;
    int range = value+valueCarry <= 3500 ? value+valueCarry : 0;

    rotate(radians(i));
    line(0, 0, range/10.0, 0);

    popMatrix();
  }

  for (int i = 180; i < 360; i++) {
    pushMatrix();

    int value = byteToShort(data2[(i - 180) * 2]);
    int valueCarry = byteToShort(data2[(i - 180) * 2 + 1]) * 256;
    int range = value+valueCarry <= 3500 ? value+valueCarry : 0;

    rotate(radians(i));
    line(0, 0, range/10.0, 0);

    popMatrix();
  }

  pop();
}
