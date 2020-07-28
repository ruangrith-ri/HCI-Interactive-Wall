package application.style;

import processing.core.PApplet;
import processing.core.PFont;

public class FontStyle {

    public static PFont nexaL;
    public static PFont nexaLL;
    public static PFont nexaB;
    public static PFont nexaBL;

    public static void FontStyle(final PApplet parent){
        nexaL = parent.createFont("font/NexaDemo-Light.otf", 11);
        nexaLL = parent.createFont("font/NexaDemo-Light.otf", 20);
        nexaB = parent.createFont("font/NexaDemo-Bold.otf", 11);
        nexaBL = parent.createFont("font/NexaDemo-Bold.otf", 20);
    }
}
