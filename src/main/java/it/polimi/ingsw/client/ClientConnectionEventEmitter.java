package it.polimi.ingsw.client;

import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.client.event.ClientConnectionEventListener;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.server.message.GameMessage;
import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.ViewEvent;

public class ClientConnectionEventEmitter extends EventEmitter {

    public void emitEvent(GameMessage message){
        executeEventListeners(ClientConnectionEventListener.class, (listener)->{
            listener.handleEvent(message);
        });
    }

    public void emitEvent(ClientConnectionEvent message){
        executeEventListeners(ClientConnectionEventListener.class, (listener)->{
            listener.handleEvent(message);
        });
    }

}
