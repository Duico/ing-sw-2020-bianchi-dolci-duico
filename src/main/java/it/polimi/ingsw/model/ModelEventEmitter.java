package it.polimi.ingsw.model;

import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.model.event.WorkerModelEvent;

public class ModelEventEmitter extends EventEmitter {//for View
    void emitEvent(WorkerModelEvent evt){
        executeEventListeners( (listener)->{
          //  ((RemoteView) listener).(evt);
        });
    }


}
