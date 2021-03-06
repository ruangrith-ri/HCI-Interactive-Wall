package application.sketch;

import application.animation.Circle;
import application.animation.Snake;
import de.looksgood.ani.Ani;
import de.looksgood.ani.AniConstants;
import jto.processing.sketch.mapper.AbstractSketch;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.text.NumberFormat;

import static application.applet.NodeControllerApplet.*;
import static application.service.ArtNetService.lidarNode;
import static application.style.FontCatalog.*;


public class VisualizationLIDAR extends AbstractSketch {

    //Scale Dis = 1/2 Radar
    final int colorIncrese = 16777215 / 16;

    Snake[] pingBall = new Snake[16];

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

        pingBall[0] = new Snake(20, 0x00FF00);

        for (int i = 1; i < 16; i++) {
            pingBall[i] = new Snake(20, 0xFF0000);
            directionPing[i] = 1;
            pingX[i] = 0;
        }
    }

    @Override
    public void draw() {
        graphics.beginDraw();

        graphics.background(0);

        graphics.translate(300, 300);
        graphics.rotate(radians(270));
        animationControl();

        for (Circle r : radar) {
            r.draw(graphics);
        }


        graphics.strokeWeight(1);
        graphics.stroke(0, radarLineColor, 0, 100);

        graphics.pushMatrix();

        graphics.rotate(radians(-4));
        for (int i = 0; i < 360; i++) {
            graphics.pushMatrix();

            //int reverse = (int) map(i, 0, 359, 359, 0);
            graphics.rotate(radians(359-i));
            graphics.line(60, 0, lidarNode.getDistance(i) / 2, 0);

            graphics.popMatrix();
        }
        graphics.popMatrix();


        drawText();
        drawPing();


        graphics.endDraw();
    }

    private void drawText() {
        graphics.pushMatrix();
        graphics.pushStyle();

        graphics.fill(textColor);

        graphics.textAlign(CENTER, CENTER);
        graphics.textFont(nexaBLL);

        graphics.text("This is LIDAR", 0, -120);

        graphics.translate(0, 0);

        graphics.textFont(nexaBL2);
        graphics.textAlign(LEFT, TOP);

        graphics.text("LIDAR is ToF (Time-of-Flight) Sensor", -250, 50);

        NumberFormat formatter =  NumberFormat.getInstance();
        formatter.setMinimumFractionDigits(2);


        graphics.text("TimeFlight  =  " + formatter.format((lidarNode.getDistance(0) / 1000f) * 2.0 / 299792458.0 *1000000000)+ "  Nano Second", -250, 115);
        graphics.textFont(nexaBLL);
        graphics.text("Distance  =  " + lidarNode.getDistance(0) / 1000f + "  Meter", -250, 150);

        graphics.popStyle();
        graphics.popMatrix();
    }

    private void drawPing() {
        for (int i = 0; i < 16; i++) {
            graphics.pushMatrix();

            graphics.rotate(radians(-22 * i));
            pingBall[i].draw(graphics, pingX[i], 0, i == 0 ? diameterPing : diameterPing2);

            graphics.popMatrix();
        }
    }

    private long lastTime = 0;
    private int[] directionPing = new int[16];
    private int[] pingX = new int[16];

    public int textColor = 0;
    public float speedPing = 5;
    public float rotatePing = 0;
    private boolean tc = false;

    public int diameterPing = 0;
    public int diameterPing2 = 0;
    public int radarLineColor = 0;
    boolean dp = false;

    public void animationControl() {
        if (lidarNode.getLidarOn() && (!lidarIsPlay)) {
            lastTime = System.currentTimeMillis();

            if (scene == 0 && (0 != lastScene)) {
                Ani.to(this, 2, "diameterPing", 20, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "textColor", 255, AniConstants.EXPO_OUT);
                Ani.to(this, 2, "diameterPing2", 0, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "radarLineColor", 0, AniConstants.LINEAR);

                System.out.println(lastScene + " " + scene + "Change to " + 0);

//                diameterPing = 20;
//                diameterPing2 = 0;
//                textColor = 255;
//                radarLineColor = 0;

                lastScene = scene;

                for (Circle r : radar) {
                    r.end();
                }
            }

            if (scene == 1 && (1 != lastScene)) {
                Ani.to(this, 1, "textColor", 0, AniConstants.EXPO_OUT);
                Ani.to(this, 2, "diameterPing", 20, AniConstants.BOUNCE_OUT);
                Ani.to(this, 2, "diameterPing2", 20, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "radarLineColor", 0, AniConstants.LINEAR);

                System.out.println(lastScene + " " + scene + "Change to " + 1);

//                diameterPing = 20;
//                diameterPing2 = 20;
//                textColor = 0;
//                radarLineColor = 0;

                lastScene = scene;

                for (Circle r : radar) {
                    r.end();
                }
            }

            if (scene == 2 && (2 != lastScene)) {
                Ani.to(this, 1, "textColor", 0, AniConstants.EXPO_OUT);
                Ani.to(this, 1, "diameterPing2", 0, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "diameterPing", 0, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "radarLineColor", 255, AniConstants.LINEAR);

                System.out.println(lastScene + " " + scene + "Change to " + 2);

//                diameterPing = 0;
//                diameterPing2 = 0;
//                textColor = 0;
//                radarLineColor = 255;

                lastScene = scene;

                for (Circle r : radar) {
                    r.start();
                }
            }

            lidarIsPlay = true;
        } else if ((!lidarNode.getLidarOn()) && lidarIsPlay) {

            Ani.to(this, 2, "diameterPing", 0, AniConstants.BOUNCE_OUT);
            Ani.to(this, 1, "textColor", 0, AniConstants.EXPO_OUT);
            Ani.to(this, 2, "diameterPing2", 0, AniConstants.BOUNCE_OUT);
            Ani.to(this, 1, "radarLineColor", 0, AniConstants.LINEAR);

            for (Circle r : radar) {
                r.end();
            }
            lidarIsPlay = false;

        } else if (lidarIsPlay) {

            if (scene == 0 && (0 != lastScene)) {
                Ani.to(this, 2, "diameterPing", 20, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "textColor", 255, AniConstants.EXPO_OUT);
                Ani.to(this, 2, "diameterPing2", 0, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "radarLineColor", 0, AniConstants.LINEAR);

                System.out.println(lastScene + " " + scene + "Change to " + 0);

//                diameterPing = 20;
//                diameterPing2 = 0;
//                textColor = 255;
//                radarLineColor = 0;

                lastScene = scene;

                for (Circle r : radar) {
                    r.end();
                }
            }

            if (scene == 1 && (1 != lastScene)) {
                Ani.to(this, 1, "textColor", 0, AniConstants.EXPO_OUT);
                Ani.to(this, 2, "diameterPing", 20, AniConstants.BOUNCE_OUT);
                Ani.to(this, 2, "diameterPing2", 20, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "radarLineColor", 0, AniConstants.LINEAR);

                System.out.println(lastScene + " " + scene + "Change to " + 1);

//                diameterPing = 20;
//                diameterPing2 = 20;
//                textColor = 0;
//                radarLineColor = 0;

                lastScene = scene;

                for (Circle r : radar) {
                    r.end();
                }
            }

            if (scene == 2 && (2 != lastScene)) {
                Ani.to(this, 1, "textColor", 0, AniConstants.EXPO_OUT);
                Ani.to(this, 1, "diameterPing2", 0, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "diameterPing", 0, AniConstants.BOUNCE_OUT);
                Ani.to(this, 1, "radarLineColor", 255, AniConstants.LINEAR);

                System.out.println(lastScene + " " + scene + "Change to " + 2);

//                diameterPing = 0;
//                diameterPing2 = 0;
//                textColor = 0;
//                radarLineColor = 255;

                lastScene = scene;

                for (Circle r : radar) {
                    r.start();
                }
            }

            long time = System.currentTimeMillis();
            long deltaTime = time - lastTime;
            lastTime = time;


            for (int i = 0; i < 16; i++) {

                if (pingX[i] >= lidarNode.getDistance(constrain(i * 22 - 1, 0, 359)) / 2) {
                    directionPing[i] = -1;
                } else if (pingX[i] <= 0) {
                    directionPing[i] = 1;
                }
                pingX[i] += (deltaTime / speedPing) * directionPing[i];

            }
        }
    }

    @Override
    public void keyEvent(KeyEvent keyEvent) {

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }
}