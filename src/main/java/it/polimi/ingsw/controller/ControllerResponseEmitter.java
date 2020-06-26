package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.view.ControllerResponseListener;

/**
 * class used to emit controller responses
 */
public class ControllerResponseEmitter extends EventEmitter {
    public void emitEvent(ControllerResponse evt){
        executeEventListeners(listener -> listener.eventResponse(evt) );
    }


    private void executeEventListeners(EventHandler<ControllerResponseListener> eventHandler){
        executeEventListeners(ControllerResponseListener.class, eventHandler);
    }

}
