package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.SetUpMessageVisitor;

/**
 * Ping message: message sent from server to client to set up connection
 * Pong message: response message sent from client to server to set up connection
 * Disconnection message: message sent from server to all clients in the lobby when a single connection goes down
 * Disconnection too many players : message sent from server to client when lobby is already full
 */
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
