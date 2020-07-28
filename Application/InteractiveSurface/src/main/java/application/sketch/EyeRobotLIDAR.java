package application.sketch;

import jto.processing.sketch.mapper.AbstractSketch;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;


public class EyeRobotLIDAR extends AbstractSketch {

    public EyeRobotLIDAR(final PApplet parent, final int width, final int height) {
        super(parent, width, height);
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        graphics.beginDraw();
        graphics.background(255);
        graphics.fill(0);

        for (int i = 0; i < 100; i++) {
            graphics.ellipse(parent.random(graphics.width), parent.random(graphics.height), 25, 25);
        }

        graphics.endDraw();
    }

    @Override
    public void keyEvent(KeyEvent keyEvent) {

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }
}