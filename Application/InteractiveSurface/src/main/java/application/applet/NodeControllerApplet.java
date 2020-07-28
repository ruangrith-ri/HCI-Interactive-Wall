package application.applet;

import application.service.ArtNetService;
import application.util.Touch;

import processing.core.PApplet;

import static application.service.ArtNetService.touchNode;
import static application.style.FontStyle.FontStyle;
import static application.style.FontStyle.nexaBL;

public class NodeControllerApplet extends PApplet {

    public static PApplet processing;

    @Override
    public void settings() {
        size(800, 300, P3D);
    }

    @Override
    public void setup() {
        processing = this;

        FontStyle(this);
        ArtNetService.initialization();
    }

    @Override
    public void draw() {
        background(0);

        textFont(nexaBL);
        textAlign(LEFT, CENTER);
        text("Command form TOUCH Node" , 20, 20);

        pushMatrix();
        translate(20,50);
        textAlign(CENTER, CENTER);

        int index = 1;

        for (Touch t : touchNode.getTouchState()) {
            pushStyle();

            if (t == Touch.PRESSED) {
                fill(0, 255, 0);
            } else {
                fill(32, 32, 32);
            }

            rect(0,0,50,50,10);

            if (t == Touch.PRESSED) {
                fill(0);
            } else {
                fill(255);
            }
            text(index++ , 25, 25);

            popStyle();
            translate(60,0);
        }
        popMatrix();
    }
}
