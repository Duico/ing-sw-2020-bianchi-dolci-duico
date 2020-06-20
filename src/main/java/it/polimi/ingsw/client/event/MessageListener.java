package it.polimi.ingsw.client.event;

import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.event.Message;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.GameMessage;
import it.polimi.ingsw.server.message.SetUpMessage;

import java.util.EventListener;

public interface MessageListener extends EventListener {
    void handleEvent(Message evt);

}
