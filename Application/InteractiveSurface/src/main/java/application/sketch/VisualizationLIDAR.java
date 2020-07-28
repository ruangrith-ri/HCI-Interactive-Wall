package application.sketch;

import application.animation.Circle;
import jto.processing.sketch.mapper.AbstractSketch;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import static application.applet.NodeControllerApplet.radar;

public class VisualizationLIDAR extends AbstractSketch {

    public VisualizationLIDAR(final PApplet parent, final int width, final int height) {
        super(parent, width, height);
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        graphics.beginDraw();
        graphics.background(0);
        graphics.translate(300,300);

        for (Circle r : radar) {
            r.draw(graphics);
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