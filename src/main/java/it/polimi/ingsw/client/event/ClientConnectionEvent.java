package it.polimi.ingsw.client.event;

public class ClientConnectionEvent {
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
        DISCONNECTION,
        PING_FAIL,
        OBJECT_IO_EXCEPTION,
        ;
    }

}
