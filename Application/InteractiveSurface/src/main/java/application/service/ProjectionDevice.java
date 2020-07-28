package application.service;

import java.awt.*;

public class ProjectionDevice {

    static GraphicsDevice[] displays;

    public static void initialization() {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        displays = localGraphicsEnvironment.getScreenDevices();
    }

    public static GraphicsDevice[] getDisplay(){
        return displays;
    }
}
