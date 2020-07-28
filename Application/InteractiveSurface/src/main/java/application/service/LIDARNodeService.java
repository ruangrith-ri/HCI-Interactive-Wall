package application.service;

import java.util.logging.Logger;

import application.service.util.*;

public class LIDARNodeService implements Runnable {

    private static final Logger logger = Logger.getLogger("LIDARNodeService");

    private final short frameRate = 40;

    private boolean lidarOn = false;

    byte[] dmxData = new byte[512];
    int[] distance = new int[360];

    @Override
    public void run() {
        while (true) {
            readLIDARData();
            castLIDARState();

            try {
                Thread.sleep(1000 / frameRate);
            } catch (InterruptedException e) {
                logger.warning("LIDARNodeService Thread is interrupted when it is sleeping" + e);
            }
        }
    }

    private void readLIDARData() {
        byte[] universeData = ArtNetService.getClient().readDmxData(0, 1);

        for (int i = 0; i < 360; i++) {
            try {
                int firstData = UtilityDMX.byteToShort(universeData[i * 2]);
                int secondData = UtilityDMX.byteToShort(universeData[i * 2 + 1]);

                int value = firstData | secondData << 8;

                distance[i] = value <= 3500 ? value : 0;
            } catch (Exception ignore) {
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

    public int[] getDistance() {
        return distance;
    }

    public int getDistance(int degreeIndex) {
        return distance[degreeIndex];
    }
}
