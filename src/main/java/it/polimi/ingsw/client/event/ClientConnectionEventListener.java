package it.polimi.ingsw.client.event;

import java.util.EventListener;

public interface ClientConnectionEventListener extends EventListener {
    void handleEvent(ClientConnectionEvent evt);
}
