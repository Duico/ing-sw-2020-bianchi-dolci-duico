
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

    public void emitEvent(WinModelEvent evt){
        executeEventListeners((listener)->{
            listener.winGame(evt);
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

    public void emitEvent(PlayerDefeatModelEvent evt){
        executeEventListeners((listener)->{
            listener.defeatPlayer(evt);
        });
    }

    public void emitEvent(FullInfoModelEvent evt){
        executeEventListeners((listener)->{
            listener.fullInfo(evt);
        });
    }

    public void emitEvent(NewChoseCardTurnModelEvent evt){
        executeEventListeners((listener)->{
            listener.newCardsTurn(evt);
        });
    }

    public void emitEvent(UndoModelEvent evt){
        executeEventListeners((listener)->{
            listener.undo(evt);
        });
    }

    private void executeEventListeners(EventHandler<ModelEventListener> eventHandler){
        executeEventListeners(ModelEventListener.class, eventHandler);
    }


}

