package it.polimi.ingsw.client;

import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.client.event.MessageListener;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.server.message.GameMessage;

public class ClientConnectionEventEmitter extends EventEmitter {

    public void emitEvent(GameMessage message){
        executeEventListeners(MessageListener.class, (listener)->{
            listener.handleEvent(message);
        });
    }

    public void emitEvent(ClientConnectionEvent message){
        executeEventListeners(MessageListener.class, (listener)->{
            listener.handleEvent(message);
        });
    }

}
