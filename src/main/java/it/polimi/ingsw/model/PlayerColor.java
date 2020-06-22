package it.polimi.ingsw.model;
import java.util.Random;

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

    /**
     * picks random color from (Yellow, Blue, Gray, White)
     * @return color randomly picked
     */
    public static PlayerColor randomEnum(){
        int x = new Random().nextInt(PlayerColor.class.getEnumConstants().length);
        return PlayerColor.class.getEnumConstants()[x];
    }

}
