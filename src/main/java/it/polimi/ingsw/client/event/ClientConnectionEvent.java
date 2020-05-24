package it.polimi.ingsw.client.event;

import it.polimi.ingsw.client.ClientConnectionEventVisitor;
import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.SetUpMessageVisitor;
import it.polimi.ingsw.event.Message;

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
        CONNECTION_LOST
        ;
    }


    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(ClientConnectionEventVisitor visitor){
        visitor.visit(this);
    }
}
