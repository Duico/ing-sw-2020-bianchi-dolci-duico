
package it.polimi.ingsw.model;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.view.ModelEventListener;

public class ModelEventEmitter extends EventEmitter {//for View

    //Removed all the single events in favor of a single function
    public void emitEvent(ModelEvent evt){
        executeEventListeners(listener -> listener.sendMessage(evt) );
    }
    public void emitPlayerDefeat(Player player){ executeEventListeners(listener -> listener.removeDefeatedPlayer(player) );}

    private void executeEventListeners(EventHandler<ModelEventListener> eventHandler){
        executeEventListeners(ModelEventListener.class, eventHandler);
    }


}

