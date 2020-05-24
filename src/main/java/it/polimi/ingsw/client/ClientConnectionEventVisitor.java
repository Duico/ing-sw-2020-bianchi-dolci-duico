package it.polimi.ingsw.client;

import it.polimi.ingsw.client.event.ClientConnectionEvent;

public abstract class ClientConnectionEventVisitor  {
    public abstract void visit(ClientConnectionEvent evt);
}
