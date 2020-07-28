package application.applet;

import application.service.ArtNetService;

import processing.core.PApplet;

import static application.service.ArtNetService.lidarNode;
import static application.service.ArtNetService.touchNode;
import static application.style.FontStyle.FontStyle;
import static application.style.FontStyle.nexaBL;

public class NodeControllerApplet extends PApplet {

    public static PApplet processing;

    @Override
    public void settings() {
        size(800, 600, P3D);
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

        noStroke();

        text("Command form TOUCH Node", 20, 20);
        playLidar(touchNode.getElectrodeTouch(0));
        touchNodeStatusDraw();

        text("Command to LIDAR Node", 20, 150);
        lidarNodeStatusDraw();
    }

    final long lidarAnimationTime = 120 * 1000;
    long lastLIDARPlay = 0;

    private void playLidar(boolean command) {
        println(timerAnimationLIDAR());

        if (timerAnimationLIDAR() < 1) {
            if (command) {
                lidarNode.setLidarOn(true);
                lastLIDARPlay = System.currentTimeMillis();
            } else {
                lidarNode.setLidarOn(false);
            }
        }
    }

    private long timerAnimationLIDAR() {
        return (long) constrain(lidarAnimationTime - (System.currentTimeMillis() - lastLIDARPlay), 0, lidarAnimationTime);
    }

    private void lidarNodeStatusDraw() {
        lidarNodeCommandDraw();
        lidarTimerDraw();
    }

    private void lidarTimerDraw() {
        pushMatrix();
        pushStyle();

        translate(140, 170);
        textAlign(LEFT, CENTER);

        fill(32, 32, 32);
        rect(0, 0, 590, 50, 5);

        fill(0, 255, 0,40);

        int bar = (int) map(timerAnimationLIDAR(), 0, lidarAnimationTime, 0, 590);
        rect(0, 0, bar, 50, constrain(bar-585,0,5));

        fill(255);

        if (lidarNode.getLidarOn()) {
            text(String.valueOf(timerAnimationLIDAR()/1000.0), 260, 25);
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

        translate(20, 170);
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

        translate(20, 50);
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