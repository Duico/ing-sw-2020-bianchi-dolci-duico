package it.polimi.ingsw.message;

import java.io.Serializable;
import java.util.ArrayList;

public class SetUpMessage implements Serializable {
    private final SetUpType type;
    private final ArrayList<String> message;

    public SetUpMessage(SetUpType type, ArrayList<String> message){
        this.type=type;
        this.message=message;
    }

    public SetUpType getType() {
        return type;
    }

    public ArrayList<String> getMessage() {
        return message;
    }
}
