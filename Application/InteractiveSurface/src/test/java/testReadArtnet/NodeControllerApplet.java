package testReadArtnet;

import ch.bildspur.artnet.ArtNetClient;
import processing.core.PApplet;

import static application.util.UtilityDMX.byteToShort;
import static application.style.FontStyle.FontStyle;
import static application.style.FontStyle.nexaBL;

public class NodeControllerApplet extends PApplet {

    public static PApplet processing;

    ArtNetClient artnet;
    byte[] dmxData = new byte[512];

    @Override
    public void settings() {
        size(500, 300, P3D);
    }

    @Override
    public void setup() {
        processing = this;

        FontStyle(this);

        artnet = new ArtNetClient();
        artnet.start();
    }

    @Override
    public void draw() {
        background(0);
        textFont(nexaBL);

        artnet.broadcastDmx(0, 0, dmxData);

        byte[] data = artnet.readDmxData(0, 3);
        int command = byteToShort(data[0]);
        text("Command form TOUCH Node: " + command, 25, 25);

    }
}
