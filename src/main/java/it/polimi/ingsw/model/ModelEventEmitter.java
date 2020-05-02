
package it.polimi.ingsw.model;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.view.ModelEventListener;

public class ModelEventEmitter extends EventEmitter {//for View
    public void emitEvent(MoveWorkerModelEvent evt){
        executeEventListeners((listener)->{
            listener.movement(evt);
        });
    }


    public void emitEvent(BuildWorkerModelEvent evt){
        executeEventListeners((listener)->{
            listener.build(evt);
        });
    }

    public void emitEvent(PlaceWorkerModelEvent evt){
        executeEventListeners((listener)->{
            listener.place(evt);
        });
    }

    public void emitEvent(NewTurnModelEvent evt){
        executeEventListeners((listener)->{
            listener.newTurn(evt);
        });
    }

    public void emitEvent(ChosenCardsModelEvent evt){
        executeEventListeners((listener)->{
            listener.chosenCards(evt);
        });
    }

    public void emitEvent(SetCardModelEvent evt){
        executeEventListeners((listener)->{
            listener.setCard(evt);
        });
    }

    public void emitEvent(PlayerRemovalModelEvent evt){
        executeEventListeners((listener)->{
            listener.removePlayer(evt);
        });
    }

    private void executeEventListeners(EventHandler<ModelEventListener> eventHandler){
        executeEventListeners(ModelEventListener.class, eventHandler);
    }


}

