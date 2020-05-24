package it.polimi.ingsw.client.event;

import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.GameMessage;
import it.polimi.ingsw.server.message.SetUpMessage;

import java.util.EventListener;

public interface ClientConnectionEventListener extends EventListener {
    void handleEvent(ClientConnectionEvent evt);
    void handleEvent(GameMessage evt);

}
