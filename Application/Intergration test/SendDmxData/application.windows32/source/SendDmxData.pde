import ch.bildspur.artnet.*;

ArtNetClient artnet;
byte[] dmxData = new byte[512];

void setup()
{
  size(500, 250);
  frameRate(40);
  colorMode(HSB, 360, 100, 100);
  textAlign(CENTER, CENTER);
  textSize(20);

  // create artnet client without buffer (no receving needed)
  artnet = new ArtNetClient(null);
  artnet.start();
}

void draw()
{
  // create color
  int c = (int)map(mouseX, 0, width, 0, 255);

  background(c);

  // fill dmx array
  dmxData[0] = (byte) c;

  // send dmx to localhost
  artnet.broadcastDmx(0, 0, dmxData);

  // show values
  text("Value : " + c, width / 2, height / 2);
}
