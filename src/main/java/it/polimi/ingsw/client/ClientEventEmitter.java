package it.polimi.ingsw.client;

import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.ViewEvent;

/**
 * class that allows client to emit events from view
 */
public class ClientEventEmitter extends EventEmitter {

    /**
     * emits generic view event
     * @param message event sent from client's view
     */
    protected void emitViewEvent(ViewEvent message){
        executeEventListeners(ViewEventListener.class, (listener)->{
            listener.handleEvent(message);
        });
    }

    /**
     * emits sign up event
     * @param message event sent from client's view
     */
    protected void emitSignUp(SignUpMessage message){
        executeEventListeners(SignUpListener.class, (listener)->{
            listener.handleEvent(message);
        });
    }
}
