package application.sketch;

import de.looksgood.ani.Ani;
import de.looksgood.ani.AniConstants;
import jto.processing.sketch.mapper.AbstractSketch;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import static application.applet.NodeControllerApplet.scene;
import static application.service.ArtNetService.lidarNode;
import static processing.core.PApplet.radians;


public class EyeRobotLIDAR extends AbstractSketch {

    PApplet parent;
    private final int width, height;

    private boolean lidarIsPlay = false;

    int diameter = 0;
    int diameter1 = 0;
    int diameter2 = 0;

    public EyeRobotLIDAR(final PApplet parent, final int width, final int height) {
        super(parent, width, height);

        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        graphics.beginDraw();
        graphics.background(0);
        graphics.translate(150, 150);

        graphics.rotate(radians(90));

        animationControl();

        graphics.fill(255);
        graphics.ellipse(0, 0, diameter2, diameter2);

        graphics.translate(300 + 150, 0);
        graphics.ellipse(0, 0, diameter1, diameter1);

        graphics.translate(300 + 150, 0);
        graphics.ellipse(0, 0, diameter, diameter);

        graphics.endDraw();
    }

    public void animationControl() {
        if (/*lidarNode.getLidarOn() && */(!lidarIsPlay)) {
            Ani.to(this, 1, "diameter2", 300, AniConstants.BOUNCE_OUT, "onEnd:activeIn");
            Ani.to(this, 1, "diameter1", 300, AniConstants.BOUNCE_OUT);
            Ani.to(this, 1, "diameter", 300, AniConstants.BOUNCE_OUT);
            lidarIsPlay = true;
        } else if ((!lidarNode.getLidarOn()) && lidarIsPlay) {
            // Ani.to(this, 2, 3, "diameter", 0, AniConstants.BOUNCE_IN_OUT);
            //Ani.to(this, 2, 3, "diameter1", 0, AniConstants.BOUNCE_IN_OUT);
            //Ani.to(this, 2, 3, "diameter2", 0, AniConstants.BOUNCE_IN_OUT);
            lidarIsPlay = false;
        } else if (lidarIsPlay) {

        }
    }

    void activeIn() {
        if (lidarIsPlay) {
            switch (scene) {
                case 0:
                    Ani.to(this, 1, "diameter2", 100, AniConstants.EXPO_IN, "onEnd:activeOut");
                    Ani.to(this, 1, "diameter1", 100, AniConstants.EXPO_IN);
                    Ani.to(this, 1, "diameter", 200, AniConstants.EXPO_IN);
                    break;

                case 1:
                    Ani.to(this, 1, "diameter2", 100, AniConstants.EXPO_IN, "onEnd:activeOut");
                    Ani.to(this, 1, "diameter1", 200, AniConstants.EXPO_IN);
                    Ani.to(this, 1, "diameter", 100, AniConstants.EXPO_IN);
                    break;

                case 2:
                    Ani.to(this, 1, "diameter2", 200, AniConstants.EXPO_IN, "onEnd:activeOut");
                    Ani.to(this, 1, "diameter1", 100, AniConstants.EXPO_IN);
                    Ani.to(this, 1, "diameter", 100, AniConstants.EXPO_IN);
                    break;

            }
        }else if (!lidarIsPlay){
            Ani.to(this, 1, "diameter2", 100, AniConstants.EXPO_IN, "onEnd:activeOut");
            Ani.to(this, 1, "diameter1", 100, AniConstants.EXPO_IN);
            Ani.to(this, 1, "diameter", 100, AniConstants.EXPO_IN);
        }
    }

    void activeOut() {
        if (lidarIsPlay) {
            switch (scene) {
                case 0:
                    Ani.to(this, 1, "diameter2", 150, AniConstants.EXPO_IN, "onEnd:activeIn");
                    Ani.to(this, 1, "diameter1", 150, AniConstants.EXPO_IN);
                    Ani.to(this, 1, "diameter", 300, AniConstants.EXPO_IN);
                    break;

                case 1:
                    Ani.to(this, 1, "diameter2", 150, AniConstants.EXPO_IN, "onEnd:activeIn");
                    Ani.to(this, 1, "diameter1", 300, AniConstants.EXPO_IN);
                    Ani.to(this, 1, "diameter", 150, AniConstants.EXPO_IN);
                    break;

                case 2:
                    Ani.to(this, 1, "diameter2", 300, AniConstants.EXPO_IN, "onEnd:activeIn");
                    Ani.to(this, 1, "diameter1", 150, AniConstants.EXPO_IN);
                    Ani.to(this, 1, "diameter", 150, AniConstants.EXPO_IN);
                    break;

            }
        }else if (!lidarIsPlay){
            Ani.to(this, 1, "diameter2", 150, AniConstants.EXPO_IN, "onEnd:activeIn");
            Ani.to(this, 1, "diameter1", 150, AniConstants.EXPO_IN);
            Ani.to(this, 1, "diameter", 150, AniConstants.EXPO_IN);
        }
    }

    @Override
    public void keyEvent(KeyEvent keyEvent) {

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }
}