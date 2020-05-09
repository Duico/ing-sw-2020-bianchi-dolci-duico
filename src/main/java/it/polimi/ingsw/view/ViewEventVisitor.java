package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameViewEventListener;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.view.event.*;

public class ViewEventVisitor extends EventEmitter {

    public void emitEvent(MoveViewEvent evt){
        executeEventListeners((listener)-> listener.move(evt));
    }

    public void emitEvent(ChallengerCardViewEvent evt){
         executeEventListeners((listener)-> listener.challengerCards(evt));
    }

    public void emitEvent(BuildViewEvent evt){
        executeEventListeners( (listener)-> listener.build(evt));
    }

    public void emitEvent(PlaceViewEvent evt){
        executeEventListeners((listener)-> listener.place(evt));
    }

    public void emitEvent(FirstPlayerViewEvent evt){ executeEventListeners((listener)-> listener.setFirstPlayer(evt)); }

    public void emitEvent(CardViewEvent evt){
        executeEventListeners((listener)-> listener.setPlayerCard(evt));
    }

    public void emitEvent(UndoViewEvent evt){
        executeEventListeners((listener)-> listener.undo(evt));
    }

    public void emitEvent(EndTurnViewEvent evt){
        executeEventListeners((listener)-> listener.endTurn(evt));
    }

    public void emitEvent(InfoViewEvent evt) { executeEventListeners((listener -> listener.requiredTurnInfo(evt)));}

    private void executeEventListeners(EventHandler<GameViewEventListener> eventHandler){
         executeEventListeners(GameViewEventListener.class, eventHandler);
    }



}



