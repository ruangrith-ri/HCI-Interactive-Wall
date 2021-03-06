package application.style;

import processing.core.PApplet;
import processing.core.PFont;

public class FontCatalog {

    public static PFont nexaL;
    public static PFont nexaLL;
    public static PFont nexaB;
    public static PFont nexaBL;
    public static PFont nexaBL2;
    public static PFont nexaBLL;

    public static void FontStyle(final PApplet parent){
        nexaL = parent.createFont("font/NexaDemo-Light.otf", 14);
        nexaLL = parent.createFont("font/NexaDemo-Light.otf", 22);
        nexaB = parent.createFont("font/NexaDemo-Bold.otf", 12);
        nexaBL = parent.createFont("font/NexaDemo-Bold.otf", 18);
        nexaBL2 = parent.createFont("font/NexaDemo-Bold.otf", 30);
        nexaBLL = parent.createFont("font/NexaDemo-Bold.otf", 40);
    }
}
