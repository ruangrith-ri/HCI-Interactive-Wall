package application.sketch;

import de.looksgood.ani.Ani;
import de.looksgood.ani.AniConstants;
import jto.processing.sketch.mapper.AbstractSketch;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import static application.service.ArtNetService.lidarNode;


public class EyeRobotLIDAR extends AbstractSketch {

    PApplet parent;
    private final int width, height;

    private boolean lidarIsPlay = false;

    int diameter = 0;

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
        graphics.translate(width / 2, height / 2);

        animationControl();

        graphics.fill(255);
        graphics.ellipse(0, 0, diameter, diameter);

        graphics.endDraw();
    }

    public void animationControl() {
        if (lidarNode.getLidarOn() && (! lidarIsPlay)) {
            Ani.to(this, 2, "diameter", 300, AniConstants.BOUNCE_OUT,"onEnd:activeIn");
            lidarIsPlay = true;
        } else if ((! lidarNode.getLidarOn()) && lidarIsPlay) {
            Ani.to(this, 2, 3, "diameter", 0, AniConstants.BOUNCE_IN_OUT);
            lidarIsPlay = false;
        } else if (lidarIsPlay) {

        }
    }

    void activeIn() {
        if (lidarIsPlay)
            Ani.to(this, 1, "diameter", 200, AniConstants.EXPO_IN,"onEnd:activeOut");
    }

    void activeOut() {
        if (lidarIsPlay)
            Ani.to(this, 1, "diameter", 300, AniConstants.EXPO_OUT,"onEnd:activeIn");
    }

    @Override
    public void keyEvent(KeyEvent keyEvent) {

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }
}