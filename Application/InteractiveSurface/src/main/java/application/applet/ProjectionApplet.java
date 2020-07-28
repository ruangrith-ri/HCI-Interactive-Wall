package application.applet;

import application.sketch.EyeRobotLIDAR;
import application.sketch.TriggerLIDAR;
import application.sketch.VisualizationLIDAR;

import jto.processing.sketch.mapper.SketchMapper;

import processing.core.PApplet;

public class ProjectionApplet extends PApplet {

    public static PApplet processing;

    private SketchMapper sketchMapper;

    static int widthSketch = 0;
    static int heightSketch = 0;

    public static void main(String[] args){
        PApplet.main("MainClass", args);
    }

    public static void setSizeSketch(int width, int height){
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

        sketchMapper = new SketchMapper(this);

        //sketchMapper.addSketch(new TriggerLIDAR(this, width / 2, height / 2));
        sketchMapper.addSketch(new VisualizationLIDAR(this, 600, 600));
        //sketchMapper.addSketch(new EyeRobotLIDAR(this, width / 2, height / 2));
    }

    @Override
    public void draw() {
        sketchMapper.draw();
    }
}
