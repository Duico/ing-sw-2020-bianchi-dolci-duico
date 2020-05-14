package it.polimi.ingsw.server.message;

import java.io.Serializable;

public class ConnectionMessage implements Serializable {
    private Type type;
    public ConnectionMessage(Type type){
        this.type=type;
    }
    public enum Type{
        PING,
        PONG,
        DISCONNECTION,
        DISCONNECTION_TOO_MANY_PLAYERS
    }

    public Type getType(){
        return this.type;
    }


}
