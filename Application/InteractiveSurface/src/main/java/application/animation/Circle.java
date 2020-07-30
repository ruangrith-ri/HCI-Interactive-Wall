package application.animation;

import de.looksgood.ani.Ani;
import processing.core.PGraphics;

import static processing.core.PApplet.constrain;

public class Circle {
    Ani diameterAnimation;

    int diameter = 0;
    int target, time, dist;

    public Circle(float dia,float dist, float time) {
        target = (int) dia;
        this.dist = (int) dist;
        this.time = (int) time;
    }

    public Circle(float dia, float time) {
        target = (int) dia;
        this.dist = (int) dia;
        this.time = (int) time;
    }

    public void start() {
        diameterAnimation = Ani.to(this, time, "diameter", target, Ani.ELASTIC_OUT);
    }

    public void start(int delay) {
        diameterAnimation = Ani.to(this, time,delay, "diameter", target, Ani.ELASTIC_OUT);
    }

    public void end() {
        diameterAnimation = Ani.to(this, time, "diameter", 0, Ani.ELASTIC_OUT);
    }

    public void draw(PGraphics graphic) {
        graphic.pushMatrix();
        graphic.pushStyle();

        graphic.noFill();
        graphic.strokeWeight(2);
        graphic.stroke(128);
        graphic.ellipse(0, 0, diameter, diameter);

        graphic.fill(constrain(diameter * 5, 0, 255));
        graphic.text(dist / 2 + " cm", 0, - (target/2f + 10));

        graphic.popStyle();
        graphic.popMatrix();
    }
}
