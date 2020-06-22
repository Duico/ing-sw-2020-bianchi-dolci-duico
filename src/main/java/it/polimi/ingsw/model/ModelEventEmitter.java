
package it.polimi.ingsw.model;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.view.ModelEventListener;

/**
 * allows to emit different types of ModelEvent events with a single function
 * ModelEvent event are sent to View
 */
public class ModelEventEmitter extends EventEmitter {

    /**
     * emits a Model event to View
     * @param evt event from Model which has to be sent to View
     */
    public void emitEvent(ModelEvent evt){
        executeEventListeners(listener -> listener.sendMessage(evt) );
    }

    /**
     * emits special event to View when a player has been defeated
     * @param player the player who has lost the game
     */
    public void emitPlayerDefeat(Player player){ executeEventListeners(listener -> listener.removeDefeatedPlayer(player) );}

    private void executeEventListeners(EventHandler<ModelEventListener> eventHandler){
        executeEventListeners(ModelEventListener.class, eventHandler);
    }


}

