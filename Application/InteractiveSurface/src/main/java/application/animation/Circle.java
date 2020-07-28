package application.animation;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PGraphics;

import static processing.core.PApplet.constrain;

public class Circle {
    Ani diameterAnimation;

    int diameter = 0;
    int target, time;

    public Circle(float d, float t) {
        target = (int) d;
        time = (int) t;
    }

    public void start() {
        diameterAnimation = Ani.to(this, time, "diameter", target, Ani.ELASTIC_OUT);
    }

    public void end() {
        diameterAnimation.reverse();
        diameterAnimation.start();
    }

    public void draw(PGraphics graphic) {
        graphic.pushMatrix();
        graphic.pushStyle();

        graphic.noFill();
        graphic.strokeWeight(1);
        graphic.stroke(128);
        graphic.ellipse(0, 0, diameter, diameter);

        graphic.fill(constrain(diameter * 5, 0, 255));
        graphic.text(target / 2 + " cm", 0, - (diameter / 2));

        graphic.popStyle();
        graphic.popMatrix();
    }
}
