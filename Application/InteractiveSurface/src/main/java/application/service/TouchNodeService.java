package application.service;

import java.util.logging.Logger;

import application.service.util.*;


public class TouchNodeService implements Runnable {

    private static final Logger logger = Logger.getLogger("TouchNodeService");

    private final short frameRate = 40;

    private boolean[] electrodeIsTouch = new boolean[12];

    byte[] dmxData = new byte[512];

    @Override
    public void run() {
        while (true) {
            mapTouchState();

            try {
                Thread.sleep(1000 / frameRate);
            } catch (InterruptedException e) {
                logger.warning("TouchNodeService Thread is interrupted when it is sleeping" + e);
            }
        }
    }

    private void mapTouchState() {
        byte[] universeData = ArtNetService.getClient().readDmxData(0, 3);

        for (int i = 0; i < 12; i++) {
            electrodeIsTouch[i] = UtilityDMX.byteToShort(universeData[i]) > 128;
        }
    }

    public boolean[] getElectrodeTouch(){
        return electrodeIsTouch;
    }

    public boolean getElectrodeTouch(int point){
        return electrodeIsTouch[point];
    }
}