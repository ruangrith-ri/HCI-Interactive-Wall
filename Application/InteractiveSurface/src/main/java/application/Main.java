package application;

import application.applet.NodeControllerApplet;
import application.applet.ProjectionApplet;
import processing.core.PApplet;

public class Main {

    public static void main(String[] args) {
        PApplet.main(NodeControllerApplet.class);
        PApplet.main(ProjectionApplet.class);
    }
}