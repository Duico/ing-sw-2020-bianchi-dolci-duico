package it.polimi.ingsw.client.event;

import it.polimi.ingsw.client.ClientConnectionEventVisitor;
import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.event.Message;

/**
 * event sent locally to notify about errors or changes occurred in the client connection
 */
public class ClientConnectionEvent extends Message {
    private Reason reason;
    public ClientConnectionEvent(Reason reason){
        this.reason = reason;
    }
    public Reason getReason() {
        return reason;
    }


    public enum Reason{
        SOCKET_EXCEPTION,
        IO_EXCEPTION,
        INTERRUPTED,
        CLOSE_IO_EXCEPTION,
        PING_FAIL,
        OBJECT_IO_EXCEPTION,
        ERROR_ON_THE_SOCKET,
        CONNECTION_LOST,
        DISCONNECTION_TOO_MANY_PLAYERS
        ;
    }


    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionEventVisitor visitor){
        visitor.visit(this);
    }

    @Override
    public String toString(){
        switch (reason){
            case SOCKET_EXCEPTION:
            case IO_EXCEPTION:
                return "Connection to the server lost (IO Exception).";
            case CONNECTION_LOST:
                return "Connection to the server lost.";
            case INTERRUPTED:
                return "Interrupted connection thread";
            case PING_FAIL:
                return "Server not reachable. Connection lost.";
            case CLOSE_IO_EXCEPTION:
                return "Error closing the object stream. End of the game.";
            case OBJECT_IO_EXCEPTION:
                return "Error sending messages to the server. End of the game.";
            case ERROR_ON_THE_SOCKET:
                return "Error opening a socket to the server.";
        }
        return "";
    }
}
