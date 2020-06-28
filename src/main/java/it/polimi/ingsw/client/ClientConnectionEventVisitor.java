package it.polimi.ingsw.client;

import it.polimi.ingsw.client.event.ClientConnectionEvent;
/**
 * class that allows to do different operations using a single method which can handle all
 * different types of ClientConnection events
 */
public interface ClientConnectionEventVisitor  {
     /** Defines behavior in case of a ClientConnectionEvent
      * @param evt ClientConnectionEvent to be visited
      */
     void visit(ClientConnectionEvent evt);
}
