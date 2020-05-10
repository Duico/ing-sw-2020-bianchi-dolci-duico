package it.polimi.ingsw.model;
import java.lang.management.PlatformLoggingMXBean;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum PlayerColor {
    WHITE,
    BLUE,
    GRAY;
    //??

    public static PlayerColor randomEnum(){
        int x = new Random().nextInt(PlayerColor.class.getEnumConstants().length);
        return PlayerColor.class.getEnumConstants()[x];
    }

}
