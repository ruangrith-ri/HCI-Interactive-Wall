package application.sketch;

import application.animation.Circle;
import application.animation.Snake;
import application.applet.NodeControllerApplet;
import de.looksgood.ani.Ani;
import de.looksgood.ani.AniConstants;
import jto.processing.sketch.mapper.AbstractSketch;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.math.BigDecimal;

import static application.applet.NodeControllerApplet.timeMillisAnimationLIDAR;
import static application.applet.NodeControllerApplet.timerMillisAnimationLIDAR;
import static application.service.ArtNetService.lidarNode;
import static application.style.FontCatalog.*;

import static processing.core.PApplet.*;
import static processing.core.PConstants.*;

public class VisualizationLIDAR extends AbstractSketch {

    //Scale Dis = 1/2 Radar

    Snake pingBall = new Snake(20, 0x00FF00);

    public Circle[] radar = new Circle[5];

    private boolean lidarIsPlay = false;

    public VisualizationLIDAR(final PApplet parent, final int width, final int height) {
        super(parent, width, height);
    }

    @Override
    public void setup() {
        for (int i = 0; i < 5; i++) {
            radar[i] = new Circle(100 + (i * 100), 20 + (i * 20), (float) (2 + (i * i / 10)));
        }
    }

    @Override
    public void draw() {
        graphics.beginDraw();
        graphics.background(0);
        graphics.translate(350, 350);

        animationControl();

        for (Circle r : radar) {
            r.draw(graphics);
        }

        graphics.strokeWeight(1);
        graphics.stroke(0, radarLineColor, 0, 100);

        for (int i = 0; i < 360; i++) {
            graphics.pushMatrix();

            graphics.rotate(radians(i));
            int reverse = (int) map(i, 0, 359, 359, 0);
            graphics.line(60, 0, lidarNode.getDistance(reverse) / 2, 0);

            graphics.popMatrix();
        }

        drawText();
        drawPing();

        graphics.endDraw();
    }

    private void drawText() {
        graphics.fill(textColor);

        graphics.textAlign(LEFT, CENTER);
        graphics.textFont(nexaBL);

        graphics.text("This is LIDAR", - 100, - 50);

        graphics.textFont(nexaBL);
        graphics.text("LIDAR is ToF Category Sensor", - 100, 50);

        graphics.textFont(nexaLL);
        graphics.text("ToF : Time-of-Flight", - 100, 75);

        graphics.textFont(nexaL);
        graphics.text("We can find distance to the object using the equation", - 100, 100);
        graphics.text("d = (c x t) / 2", - 100, 130);
        graphics.text("light travel at the speed : c = 299,792,458 m/s", - 100, 160);

        BigDecimal bd = BigDecimal.valueOf((lidarNode.getDistance(0) / 1000f) * 2.0 / 299792458.0);
        bd = bd.setScale(9, BigDecimal.ROUND_UP);
        double r = bd.doubleValue();

        graphics.text("time of light travel (Green Ball Reflect) : t = " + r, - 100, 180);

        graphics.text("d = " + "(299,792,458 x " + r + ") / 2", - 100, 220);
        graphics.text("d = " + lidarNode.getDistance(0) / 1000f + " m", - 100, 240);
    }

    private void drawPing() {
        graphics.pushStyle();
        graphics.pushMatrix();

        graphics.rotate(radians(rotatePing));
        pingBall.draw(graphics, pingX, 0, diameterPing);

        graphics.popMatrix();
        graphics.popStyle();
    }

    private long lastTime = 0;
    private int directionPing = 1;
    private int pingX = 0;

    public int textColor = 0;
    public float speedPing = 5;
    public float rotatePing = 0;
    private boolean tc = false;

    public int diameterPing = 0;
    public int radarLineColor = 0;
    boolean dp = false;

    public void animationControl() {
        if (lidarNode.getLidarOn() && (! lidarIsPlay)) {
            lastTime = System.currentTimeMillis();

            directionPing = 1;
            diameterPing = 0;
            pingX = 0;

            Ani.to(this, 2, 7, "textColor", 255, AniConstants.EXPO_OUT);
            rotatePing = 0;
            textColor = 0;
            speedPing = 5;
            tc = true;

            Ani.to(this, 2, 9, "diameterPing", 20, AniConstants.BOUNCE_OUT);
            radarLineColor = 0;
            dp = true;

            lidarIsPlay = true;
        } else if ((! lidarNode.getLidarOn()) && lidarIsPlay) {

            for (Circle r : radar) {
                r.end();
            }
            lidarIsPlay = false;
        } else if (lidarIsPlay) {
            if (timeMillisAnimationLIDAR() / 1000 > 30 && tc) {
                Ani.to(this, 1, "textColor", 0, AniConstants.EXPO_OUT);
            }

            if (timeMillisAnimationLIDAR() / 1000 > 40 && dp) {
                Ani.to(this, 1, "diameterPing", 0, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "radarLineColor", 255, AniConstants.LINEAR);

                for (Circle r : radar) {
                    r.start();
                }
            }

            long time = System.currentTimeMillis();
            long deltaTime = time - lastTime;

            if (timeMillisAnimationLIDAR() / 1000 > 30) {
                rotatePing += (360f / 10f / 1000f * deltaTime * ((30000-timerMillisAnimationLIDAR()) / 5000f));
            }

            if (pingX >= lidarNode.getDistance((int) (PApplet.constrain(rotatePing, 0, 359)))/ 2 - 10) {
                directionPing = - 1;
            } else if (pingX <= 0) {
                directionPing = 1;
            }
            pingX += (deltaTime / speedPing) * directionPing;

            System.out.println(pingX + "  -  " + (deltaTime / speedPing) * directionPing + "  -  " + directionPing + " " +  speedPing + " " + deltaTime);

            lastTime = time;
        }
    }

    @Override
    public void keyEvent(KeyEvent keyEvent) {

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }
}