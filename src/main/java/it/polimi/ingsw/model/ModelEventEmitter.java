package it.polimi.ingsw.model;

import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.model.event.WorkerModelEvent;
import it.polimi.ingsw.view.event.ViewEvent;

public class ModelEventEmitter extends EventEmitter {//for View
    public void emitEvent(ModelEvent event){
        //handle event
    }
}
