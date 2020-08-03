package application.applet;

import application.sketch.EyeRobotLIDAR;
import application.sketch.TriggerLIDAR;
import application.sketch.VisualizationLIDAR;

import ixagon.surface.mapper.SurfaceMapper;
import jto.processing.sketch.mapper.SketchMapper;

import processing.core.PApplet;

import java.util.Objects;

import static application.applet.helper.SketchMapperHelper.getSurfaceMapper;

public class ProjectionApplet extends PApplet {

    public static PApplet processing;

    private SketchMapper sketchMapper;
    private SurfaceMapper sm;

    boolean render = false;

    static int widthSketch = 0;
    static int heightSketch = 0;

    public static void main(String[] args) {
        PApplet.main("MainClass", args);
    }

    public static void setSizeSketch(int width, int height) {
        widthSketch = width;
        heightSketch = height;
    }

    @Override
    public void settings() {
        size(widthSketch, heightSketch, P3D);
    }

    @Override
    public void setup() {
        processing = this;
        blendMode(ADD);

        frameRate(60);

        sketchMapper = new SketchMapper(this, "lidarNodeMapper.xml");

        sm = Objects.requireNonNull(getSurfaceMapper(this.sketchMapper));

        sketchMapper.addSketch(new VisualizationLIDAR(this, 600, 600));
        sketchMapper.addSketch(new EyeRobotLIDAR(this, 300, 1200));
        sketchMapper.addSketch(new TriggerLIDAR(this, 1300, 300));
    }

    @Override
    public void draw() {
        sketchMapper.draw();

        if (!render){
            sm.toggleCalibration();
            render = true;
        }
    }
}
