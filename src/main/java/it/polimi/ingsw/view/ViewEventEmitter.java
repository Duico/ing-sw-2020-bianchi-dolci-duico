package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.view.event.BuildViewEvent;
import it.polimi.ingsw.view.event.MoveViewEvent;

public class ViewEventEmitter extends EventEmitter { //for Controller

    public void emitEvent(MoveViewEvent evt){
        executeEventListeners( (listener)->{
            ((Controller) listener).move(evt);
        });
    }

    public void emitEvent(BuildViewEvent evt){
        executeEventListeners( (listener)->{
            ((Controller) listener).build(evt);
        });
    }

}
