package it.polimi.ingsw.model;
import java.lang.management.PlatformLoggingMXBean;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum PlayerColor {
    YELLOW,
    BLUE,
    GRAY,
    WHITE,
    ;
    //??

    public static PlayerColor fromInt(Integer n){
        switch(n){
            case 0:
                return YELLOW;
            case 1:
                return BLUE;
            case 2:
                return GRAY;
            default:
                return WHITE;
        }
    }

    public static PlayerColor randomEnum(){
        int x = new Random().nextInt(PlayerColor.class.getEnumConstants().length);
        return PlayerColor.class.getEnumConstants()[x];
    }

}
