package application.animation;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class Snake {
    int sizeOfArray;

    int r;
    int g;
    int b;

    int distance;

    int[] xpos ;
    int[] ypos ;

    public Snake(int sizeOfArrayInput, int colorCodeInput, int distanceInput) {
        sizeOfArray = sizeOfArrayInput;
        distance = distanceInput;

        xpos = new int[sizeOfArray];
        ypos = new int[sizeOfArray];

        r = (colorCodeInput & 0xFF0000) >> 16;
        g = (colorCodeInput & 0xFF00) >> 8;
        b = (colorCodeInput & 0xFF);
    }

    public Snake(int sizeOfArrayInput, int colorCodeInput) {
        sizeOfArray = sizeOfArrayInput;
        distance = 0;

        xpos = new int[sizeOfArray];
        ypos = new int[sizeOfArray];

        r = (colorCodeInput & 0xFF0000) >> 16;
        g = (colorCodeInput & 0xFF00) >> 8;
        b = (colorCodeInput & 0xFF);
    }

    float blendFunction( float t, float n, float a, float b ) {
        float p = t/n;
        return a*p + b*(1-p);
    }

    public void draw(PGraphics graphics,int pX, int pY,int size) {
        for (int i = 0; i < xpos.length-1; i ++ ) {
            xpos[i] = xpos[i+1];
            ypos[i] = ypos[i+1];
        }

        // New location
        xpos[xpos.length-1] = pX;
        ypos[ypos.length-1] = pY;

        // Draw everything
        for (int i = 0; i < PApplet.constrain(size,1,xpos.length); i ++ ) {
            graphics.noStroke();
            graphics.fill( blendFunction( i, xpos.length, r, 255),
                    blendFunction( i, xpos.length, g, 255),
                    blendFunction( i, xpos.length, b, 255) );
            graphics.ellipse(xpos[i]+distance, ypos[i], i, i);
        }
    }
}
