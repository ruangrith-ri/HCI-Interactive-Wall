import ch.bildspur.artnet.*;

PFont nexaL;
PFont nexaLL;
PFont nexaB;
PFont nexaBL;

ArtNetClient artnet;
byte[] dmxData = new byte[1];

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
  
    drawPolar();

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

  push();

  translate(500, 500);

  fill(0, 0, 0, 0);
  
  float j = 2;

  for (int i = 50; i <=700; i += 50) {
    stroke(   constrain((((millis() - timeOnLDS)/10.0)-i/j), 0, 128)   );
    circle(0, 0, i);
    j+=0.5;
  }

  stroke(0, 255, 0);
  strokeWeight(1);

  for (int i = 0; i <360; i++) {
    pushMatrix();

    int value = byteToShort(data1[i * 2]);
    int valueCarry = byteToShort(data1[i * 2 + 1]) * 256;
    int range = value+valueCarry <= 3500 ? value+valueCarry : 0;

    rotate(radians(i));
    line(0, 0, range/10.0, 0);

    popMatrix();
  }

  pop();
}
