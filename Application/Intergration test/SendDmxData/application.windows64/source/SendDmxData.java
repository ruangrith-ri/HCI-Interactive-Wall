import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ch.bildspur.artnet.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SendDmxData extends PApplet {



ArtNetClient artnet;
byte[] dmxData = new byte[512];

public void setup()
{
  
  frameRate(40);
  colorMode(HSB, 360, 100, 100);
  textAlign(CENTER, CENTER);
  textSize(20);

  // create artnet client without buffer (no receving needed)
  artnet = new ArtNetClient(null);
  artnet.start();
}

public void draw()
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

  public void settings() {  size(500, 250); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SendDmxData" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
