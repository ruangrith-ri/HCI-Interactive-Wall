package application.service;

import application.util.Touch;

import java.util.logging.Logger;

import static application.util.UtilityDMX.byteToShort;

public class TouchNodeReceiver implements Runnable {

    private static final Logger logger = Logger.getLogger("TouchNodeReceiver");

    private final short frameRate = 30;

    private Touch[] touchState = new Touch[12];

    byte[] dmxData = new byte[512];

    @Override
    public void run() {
        while (true) {
            streamTouchState();

            ArtNetService.getClient().broadcastDmx(0, 0, dmxData);

            try {
                Thread.sleep(1000 / frameRate);
            } catch (InterruptedException e) {
                logger.warning("Kinetic Thread is interrupted when it is sleeping" + e);
            }
        }
    }

    private void streamTouchState() {
        byte[] universeData = ArtNetService.getClient().readDmxData(0, 3);

        for (int i = 0; i < 12; i++) {
            touchState[i] = byteToShort(universeData[i]) > 128 ? Touch.PRESSED : Touch.NORMAL;
        }
    }

    public Touch[] getTouchState(){
        return touchState;
    }

    public Touch getTouchState(int point){
        return touchState[point];
    }
}