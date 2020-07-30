package application.sketch;


import de.looksgood.ani.Ani;
import de.looksgood.ani.AniConstants;
import jto.processing.sketch.mapper.AbstractSketch;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import static application.service.ArtNetService.lidarNode;
import static processing.core.PApplet.radians;
import static processing.core.PApplet.sin;
import static processing.core.PConstants.TWO_PI;

public class TriggerLIDAR extends AbstractSketch {

    private boolean lidarIsPlay = false;

    PApplet parent;
    int width, height;

    int[] phaseIndex = {10, 45, 300, 127, 255};
    int waveResolution = 2;
    float theta = 0;
    float amplitude = 25;
    float period = 7000;

    int waveWidth;
    int numVertex;
    float dx;


    public TriggerLIDAR(final PApplet parent, final int width, final int height) {
        super(parent, width, height);

        this.width = width;
        this.height = height;
        this.parent = parent;

        waveWidth = width + waveResolution;
        dx = (TWO_PI / period) * waveResolution;
        numVertex = waveWidth / waveResolution;
    }

    @Override
    public void setup() {

    }


    @Override
    public void draw() {
        graphics.beginDraw();
        graphics.background(0);

        animationControl();

        graphics.strokeWeight(3);
        graphics.noFill();

        for (int j = 0; j < 5; j++) {
            graphics.stroke(255* parent.noise((float) (j / 1.0)), 255 - 255 * parent.noise((float) (j / 1.0)), 255);

            renderWave(phaseIndex[j]);
        }

        graphics.endDraw();
    }

    int signalHook = -300;

    public void animationControl() {
        if (lidarNode.getLidarOn() && (! lidarIsPlay)) {
            Ani.to(this, 4,2, "signalHook", numVertex, AniConstants.EXPO_OUT);

            lidarIsPlay = true;
        } else if ((! lidarNode.getLidarOn()) && lidarIsPlay) {
            Ani.to(this, 4,1, "signalHook", -300, AniConstants.EXPO_OUT);

            lidarIsPlay = false;
        } else if (lidarIsPlay) {

        }
    }

    void renderWave(int phase) {
        graphics.beginShape();

        theta += 0.02;
        float z = theta;

        for (int x = 0; x < signalHook + phase; x++) {
            graphics.vertex(x * waveResolution, height / 2 + sin(z + radians(phase)) * amplitude);
            z += dx;
        }

        graphics.endShape();
    }

    @Override
    public void keyEvent(KeyEvent keyEvent) {

    }

    @Override
    public void mouseEvent(MouseEvent mouseEvent) {

    }
}