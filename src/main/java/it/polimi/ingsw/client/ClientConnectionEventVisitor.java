package it.polimi.ingsw.client;

import it.polimi.ingsw.client.event.ClientConnectionEvent;

public interface ClientConnectionEventVisitor  {
     void visit(ClientConnectionEvent evt);
}
