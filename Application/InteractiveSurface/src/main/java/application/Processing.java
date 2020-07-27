package application;

import application.sketch.TriggerLIDAR;
import jto.processing.sketch.mapper.SketchMapper;
import processing.core.PApplet;

public class Processing extends PApplet {
    public static PApplet processing;

    private SketchMapper sketchMapper;

    public static void main(String[] args){
        PApplet.main("MainClass", args);
    }

    @Override
    public void settings() {
        size(600, 600, P3D);
    }

    @Override
    public void setup() {
        processing = this;

        sketchMapper = new SketchMapper(this);
        sketchMapper.addSketch(new TriggerLIDAR(this, width / 2, height / 2));
    }

    @Override
    public void draw() {
        sketchMapper.draw();
    }
}
