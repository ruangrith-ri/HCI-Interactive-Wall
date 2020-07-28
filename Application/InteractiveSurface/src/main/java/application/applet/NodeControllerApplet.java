package application.applet;

import application.animation.Circle;
import application.service.ArtNetService;
import application.service.ProjectionDevice;

import de.looksgood.ani.Ani;
import processing.core.PApplet;

import java.awt.*;

import static application.service.ArtNetService.lidarNode;
import static application.service.ArtNetService.touchNode;
import static application.style.FontStyle.*;

public class NodeControllerApplet extends PApplet {

    public static PApplet processing;

    public static Circle[] radar = new Circle[6];

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

        playLidar(touchNode.getElectrodeTouch(0));

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

    final long lidarAnimationTime = 30 * 1000;
    long lastLIDARPlay = 0;

    boolean lidarIsPlay = false;

    private void playLidar(boolean command) {

        if (timerAnimationLIDAR() < 1) {
            if (command) {
                lidarNode.setLidarOn(true);
                lastLIDARPlay = System.currentTimeMillis();

                for (Circle r : radar) {
                    r.start();
                }

                lidarIsPlay = true;

            } else if (lidarIsPlay) {
                lidarNode.setLidarOn(false);

                for (Circle r : radar) {
                    r.end();
                }

                lidarIsPlay = false;
            }
        }
    }

    private long timerAnimationLIDAR() {
        return (long) constrain(lidarAnimationTime - (System.currentTimeMillis() - lastLIDARPlay), 0, lidarAnimationTime);
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
            line(0, 0, (float) (lidarNode.getDistance(i) / 10.0), 0);

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

        int bar = (int) map(timerAnimationLIDAR(), 0, lidarAnimationTime, 0, 590);
        rect(0, 0, bar, 50, constrain(bar - 585, 0, 5));

        fill(255);

        if (lidarNode.getLidarOn()) {
            text(String.valueOf(timerAnimationLIDAR() / 1000.0), 260, 25);
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


