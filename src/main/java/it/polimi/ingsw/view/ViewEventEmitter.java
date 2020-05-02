package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameViewEventListener;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.view.event.*;

public class ViewEventEmitter extends EventEmitter {

     public void emitEvent(MoveViewEvent evt){
        executeMoveEventListeners((listener)->{
            listener.move(evt);
        });
    }

    public void emitEvent(BuildViewEvent evt){
        executeBuildEventListeners( (listener)->{
            listener.build(evt);
        });
    }

    public void emitEvent(PlaceViewEvent evt){
        executePlaceEventListeners((listener)->{
            listener.place(evt);
        });
    }

    public void emitEvent(FirstPlayerViewEvent evt){
        executeFirstPlayerEventListeners((listener)->{
            listener.setFirstPlayer(evt);
        });
    }

    public void emitEvent(CardViewEvent evt){
        executePlayerCardEventListeners((listener)->{
            listener.setPlayerCard(evt);
        });
    }

    public void emitEvent(UndoViewEvent evt){
        executeUndoEventListeners((listener)->{
            listener.undo(evt);
        });
    }

    public void emitEvent(EndTurnViewEvent evt){
        executeEndTurnEventListeners((listener)->{
            listener.endTurn(evt);
        });
    }




    private void executeMoveEventListeners(EventHandler<GameViewEventListener> eventHandler){
        executeEventListeners(GameViewEventListener.class, eventHandler);
    }

    private void executeBuildEventListeners(EventHandler<GameViewEventListener> eventHandler){
        executeEventListeners(GameViewEventListener.class, eventHandler);
    }

    private void executePlaceEventListeners(EventHandler<GameViewEventListener> eventHandler){
        executeEventListeners(GameViewEventListener.class, eventHandler);
    }

    private void executeFirstPlayerEventListeners(EventHandler<GameViewEventListener> eventHandler){
        executeEventListeners(GameViewEventListener.class, eventHandler);
    }

    private void executePlayerCardEventListeners(EventHandler<GameViewEventListener> eventHandler){
        executeEventListeners(GameViewEventListener.class, eventHandler);
    }

    private void executeUndoEventListeners(EventHandler<GameViewEventListener> eventHandler){
        executeEventListeners(GameViewEventListener.class, eventHandler);
    }

    private void executeEndTurnEventListeners(EventHandler<GameViewEventListener> eventHandler){
        executeEventListeners(GameViewEventListener.class, eventHandler);
    }




}



