package application.applet;

import application.animation.Circle;
import application.service.ArtNetService;
import application.service.ProjectionDevice;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;

import static application.service.ArtNetService.lidarNode;
import static application.service.ArtNetService.touchNode;
import static application.style.FontCatalog.*;

public class NodeControllerApplet extends PApplet {

    public class X {
        Ani diameterAnimation;

        int diameter = 0;
        int target, time;

        public X(float d, float t) {
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
            graphic.text(target / 2 + " cm", 0, -(diameter / 2));

            graphic.popStyle();
            graphic.popMatrix();
        }
    } ///Ball

    public static PApplet processing;
    public Circle[] radar = new Circle[6];

    @Override
    public void settings() {
        size(800, 1000, P3D);
    }

    @Override
    public void setup() {
        processing = this;

        FontStyle(this);

        ArtNetService.initialization();
        ProjectionDevice.initialization();

        noStroke();
        ellipseMode(CENTER);

        Ani.init(this);

        for (int i = 0; i < 6; i++) {
            radar[i] = new Circle(100 + (i * 100), (float) (2 + (i * i / 10)));
        }
    }

    @Override
    public void draw() {
        background(0);

        textFont(nexaLL);
        textAlign(LEFT, CENTER);

        text("Projection to : ", 20, 20);
        text("Command form TOUCH Node", 20, 120);
        text("Command to LIDAR Node", 20, 220);
        text("Data form LIDAR Node", 20, 320);

        projectionSelectDraw();

        playLidar(touchNode.getElectrodeTouch());

        //touchNode.getElectrodeTouch(5);

        touchNode.getElectrodeTouch(11);

        textFont(nexaBL);
        touchNodeStatusDraw();
        lidarNodeStatusDraw();
    }

    private void projectionSelectDraw() {
        pushMatrix();
        pushStyle();

        textAlign(LEFT, CENTER);

        translate(20, 40);

        int i = 0;

        for (GraphicsDevice gd : ProjectionDevice.getDisplay()) {

            if (overRect(20 + (i * 150), 40, 140, 50)) {
                fill(64);
            } else {
                fill(32);
            }

            rect(0, 0, 140, 50, 5);

            fill(255);
            textFont(nexaBL);
            text(gd.getIDstring().substring(1), 10, 15);
            textFont(nexaL);
            text(gd.getDisplayMode().getWidth() + " X " + gd.getDisplayMode().getHeight(), 10, 35);

            translate(150, 0);
            i++;
        }

        popStyle();
        popMatrix();
    }

    @Override
    public void mousePressed() {
        int i = 0;

        for (GraphicsDevice gd : ProjectionDevice.getDisplay()) {
            if (overRect(20 + (i * 150), 40, 140, 50)) {
                ProjectionApplet.setSizeSketch(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight() - 50);
                PApplet.main(ProjectionApplet.class);
            }
            i++;
        }
    }

    boolean overRect(int x, int y, int width, int height) {
        return (mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height);
    }

    static final long LIDAR_ANIMATION_TIME = 300 * 1000;
    static long lastLIDARPlay = 0;

    boolean lidarIsPlay = false;

    public static int scene = 0;
    public static int lastScene = 0;

    private void playLidar(boolean[] command) {

        if (timerMillisAnimationLIDAR() < 1) {
            if (command[0]) {
                lidarNode.setLidarOn(true);
                lastLIDARPlay = System.currentTimeMillis();

                for (Circle r : radar) {
                    r.start();
                }

                lidarIsPlay = true;
                scene = 0;

            } else if (command[5]) {
                lidarNode.setLidarOn(true);
                lastLIDARPlay = System.currentTimeMillis();

                for (Circle r : radar) {
                    r.start();
                }

                lidarIsPlay = true;
                scene = 1;

            } else if (command[11]) {
                lidarNode.setLidarOn(true);
                lastLIDARPlay = System.currentTimeMillis();

                for (Circle r : radar) {
                    r.start();
                }

                lidarIsPlay = true;
                scene = 2;

            } else if (lidarIsPlay) {
                lidarNode.setLidarOn(false);

                for (Circle r : radar) {
                    r.end();
                }

                lidarIsPlay = false;
            }
        } else if (lidarIsPlay){
            if (command[0] && (lastScene != 0)) {
                scene = 0;

                System.out.println(lastScene + " " + scene);

            } else if (command[5] && (lastScene != 1)) {
                scene = 1;

                System.out.println(lastScene + " " + scene);
            } else if (command[11]&& (lastScene != 2)) {
                scene = 2;

                System.out.println(lastScene + " " + scene);
            }
        }
    }

    public static long timerMillisAnimationLIDAR() {
        return (long) constrain(LIDAR_ANIMATION_TIME - (System.currentTimeMillis() - lastLIDARPlay), 0, LIDAR_ANIMATION_TIME);
    }

    public static long timeMillisAnimationLIDAR() {
        return (long) constrain((System.currentTimeMillis() - lastLIDARPlay), 0, LIDAR_ANIMATION_TIME);
    }

    private void lidarNodeStatusDraw() {
        pushMatrix();
        pushStyle();

        translate(20, 240);

        lidarNodeCommandDraw();
        lidarTimerDraw();
        lidarDistanceDraw();

        popStyle();
        popMatrix();
    }

    private void lidarDistanceDraw() {
        pushMatrix();
        pushStyle();

        textFont(nexaL);
        textAlign(LEFT, BOTTOM);

        translate(0, 80);
        translate(400, 350);

        stroke(128);
        strokeWeight(3);

        point(0, 0);

        noFill();
        strokeWeight(1);

        for (Circle r : radar) {
            r.draw(getGraphics());
        }

        stroke(0, 255, 0, 127);

        for (int i = 0; i < 360; i++) {
            pushMatrix();

            rotate(radians(i));
            int reverse = (int) map(i, 0, 359, 359, 0);
            line(12, 0, (float) (lidarNode.getDistance(reverse) / 10.0), 0);

            popMatrix();
        }

        popStyle();
        popMatrix();
    }

    private void lidarTimerDraw() {
        pushMatrix();
        pushStyle();

        translate(120, 0);

        textAlign(LEFT, CENTER);

        fill(32, 32, 32);
        rect(0, 0, 590, 50, 5);

        fill(0, 255, 0, 40);

        int bar = (int) map(timerMillisAnimationLIDAR(), 0, LIDAR_ANIMATION_TIME, 0, 590);
        rect(0, 0, bar, 50, constrain(bar - 585, 0, 5));

        fill(255);

        if (lidarNode.getLidarOn()) {
            text(String.valueOf(timerMillisAnimationLIDAR() / 1000.0), 260, 25);
            fill(0, 255, 0);
        } else {
            text("Off LIDAR Node", 220, 25);
            fill(255, 0, 0);
        }
        rect(0, 0, 7, 50, 5, 0, 0, 5);

        popStyle();
        popMatrix();
    }

    private void lidarNodeCommandDraw() {
        pushMatrix();
        pushStyle();

        textAlign(CENTER, CENTER);

        if (lidarNode.getLidarOn()) {
            fill(0, 255, 0);
        } else {
            fill(32, 32, 32);
        }

        rect(0, 0, 110, 50, 5);

        if (lidarNode.getLidarOn()) {
            fill(0);
            text(255, 55, 25);
        } else {
            fill(255);
            text(0, 55, 25);
        }

        popStyle();
        popMatrix();
    }

    private void touchNodeStatusDraw() {
        pushMatrix();
        pushStyle();

        translate(20, 140);
        textAlign(CENTER, CENTER);

        int index = 1;

        for (boolean t : touchNode.getElectrodeTouch()) {
            pushStyle();

            if (t) {
                fill(0, 255, 0);
            } else {
                fill(32, 32, 32);
            }

            rect(0, 0, 50, 50, 5);

            if (t) {
                fill(0);
            } else {
                fill(255);
            }
            text(index++, 25, 25);

            popStyle();
            translate(60, 0);
        }

        popStyle();
        popMatrix();
    }
}


