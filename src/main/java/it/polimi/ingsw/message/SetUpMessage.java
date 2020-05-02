package it.polimi.ingsw.message;

import java.io.Serializable;

public class SetUpMessage implements Serializable {
    private final SetUpType type;
    private final String message;

    public SetUpMessage(SetUpType type, String message){
        this.type=type;
        this.message=message;
    }

    public SetUpType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
