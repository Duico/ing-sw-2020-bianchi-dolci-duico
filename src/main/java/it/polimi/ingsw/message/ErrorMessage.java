package it.polimi.ingsw.message;

import java.io.Serializable;

public class ErrorMessage implements Serializable {
    private ErrorTyper type;
    private String message;

    public ErrorMessage(ErrorTyper type, String message){
        this.type=type;
        this.message=message;
    }

    public ErrorTyper getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
