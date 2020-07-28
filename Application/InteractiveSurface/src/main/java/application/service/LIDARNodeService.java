package application.service;

import java.util.logging.Logger;


public class LIDARNodeService implements Runnable {

    private static final Logger logger = Logger.getLogger("LIDARNodeService");

    private final short frameRate = 40;

    private boolean lidarOn = false;

    byte[] dmxData = new byte[512];

    @Override
    public void run() {
        while (true) {
            castLIDARState();

            try {
                Thread.sleep(1000 / frameRate);
            } catch (InterruptedException e) {
                logger.warning("LIDARNodeService Thread is interrupted when it is sleeping" + e);
            }
        }
    }

    private void castLIDARState() {
        if (lidarOn) {
            for (int i = 0; i < 512; i++) {
                dmxData[i] = (byte) 255;
            }
        } else {
            for (int i = 0; i < 512; i++) {
                dmxData[i] = (byte) 0;
            }
        }

        ArtNetService.getClient().broadcastDmx(0, 0, dmxData);
    }

    public void setLidarOn(boolean on) {
        lidarOn = on;
    }

    public boolean getLidarOn() {
        return lidarOn;
    }

//    public Touch[] getTouchState(){
//        return touchState;
//    }
//
//    public Touch getTouchState(int point){
//        return touchState[point];
//    }
}
