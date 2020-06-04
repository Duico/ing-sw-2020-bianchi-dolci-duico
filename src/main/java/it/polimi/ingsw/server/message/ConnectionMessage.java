package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.SetUpMessageVisitor;


public class ConnectionMessage extends GameMessage {
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

    @Override
    public void accept(MessageVisitor visitor) {

        //visitor.visit(this);
    }

    public void accept(SetUpMessageVisitor visitor){
        //visitor.visit(this);
    }
}
