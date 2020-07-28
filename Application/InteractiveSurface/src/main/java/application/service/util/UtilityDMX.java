package application.service.util;

public class UtilityDMX {
    public static short byteToShort(byte dmxByte) {
        return (short) (dmxByte & 0xFF);
    }
}
