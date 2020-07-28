package application.service;

import ch.bildspur.artnet.ArtNetBuffer;
import ch.bildspur.artnet.ArtNetClient;

public class ArtNetService {

    private static ArtNetClient artnet;

    public static TouchNodeService touchNode = new TouchNodeService();
    public static LIDARNodeService lidarNode = new LIDARNodeService();

    public static ArtNetClient getClient() {
        return artnet;
    }

    public static void initialization() {
        artnet = new ArtNetClient(new ArtNetBuffer(), 6454, 6454);
        artnet.start();

        Thread t = new Thread(touchNode);
        t.start();

        Thread l = new Thread(lidarNode);
        l.start();
    }
}
