package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameViewEventListener;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.view.event.*;

/**
 * Class that allows a client to launch any kind of game event from his view using a single function
 */
public class ViewEventVisitor extends EventEmitter {

    /**
     * emit a MoveViewEvent to listeners
     * @param evt
     */
    public void emitEvent(MoveViewEvent evt){
        executeEventListeners((listener)-> listener.move(evt));
    }

    /**
     * emit a ChallengerCardViewEvent to listeners
     * @param evt
     */
    public void emitEvent(ChallengerCardViewEvent evt){
         executeEventListeners((listener)-> listener.challengerCards(evt));
    }

    /**
     * emit a BuildViewEvent to listeners
     * @param evt
     */
    public void emitEvent(BuildViewEvent evt){
        executeEventListeners( (listener)-> listener.build(evt));
    }

    /**
     * emit a PlaceViewEvent to listeners
     * @param evt
     */
    public void emitEvent(PlaceViewEvent evt){
        executeEventListeners((listener)-> listener.place(evt));
    }

    /**
     * emit a FirstPlayerViewEvent to listeners
     * @param evt
     */
    public void emitEvent(FirstPlayerViewEvent evt){ executeEventListeners((listener)-> listener.setFirstPlayer(evt)); }

    /**
     * emit a CardViewEvent to listeners
     * @param evt
     */
    public void emitEvent(CardViewEvent evt){
        executeEventListeners((listener)-> listener.setPlayerCard(evt));
    }

    /**
     * emit an UndoViewEvent to listeners
     * @param evt
     */
    public void emitEvent(UndoViewEvent evt){
        executeEventListeners((listener)-> listener.undo(evt));
    }

    /**
     * emit an endTurnViewEvent to listeners
     * @param evt
     */
    public void emitEvent(EndTurnViewEvent evt){
        executeEventListeners((listener)-> listener.endTurn(evt));
    }

    /**
     * emit a InfoViewEvent to listeners
     * @param evt
     */
    public void emitEvent(InfoViewEvent evt) { executeEventListeners((listener -> listener.requiredTurnInfo(evt)));}

    private void executeEventListeners(EventHandler<GameViewEventListener> eventHandler){
         executeEventListeners(GameViewEventListener.class, eventHandler);
    }



}



