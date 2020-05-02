/*
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.model.Lobby;

import java.util.EventListener;


public class ViewEventEmitter extends EventEmitter {


    public void emitEvent(PingViewEvent evt){
        executeLobbyEventListeners( (listener)->{
            listener.ping(evt);
        });
    }


    public void emitEvent(MoveViewEvent evt){
        executeGameEventListeners( (listener)->{
            listener.move(evt);
        });
    }

    public void emitEvent(BuildViewEvent evt){
        executeGameEventListeners( (listener)->{
            listener.build(evt);
        });
    }

    public void emitEvent(FirstPlayerViewEvent evt){
        executeGameEventListeners( (listener)->{
            listener.setFirstPlayer(evt);
        });
    }


    public void emitEvent(JoinLobbyViewEvent evt){
        executeLobbyEventListeners( (listener)->{
            listener.addPlayer(evt);
        });
    }


    private void executeGameEventListeners(EventHandler<Controller> eventHandler){
        executeEventListeners(Controller.class, eventHandler);
    }
    private void executeLobbyEventListeners(EventHandler<LobbyController> eventHandler){
        executeEventListeners(LobbyController.class, eventHandler);
    }

}

//public class ViewEventEmitter{
//    GameViewEventEmitter gameEmitter;
//    LobbyViewEventEmitter lobbyEmitter;
//
//    public void addGameListener(){
//        gameEmitter.ass
//    }
//    public void addLobbyListener(){
//
//    }
//
//    public void emitEvent(MoveViewEvent evt){
//        gameEmitter.executeEventListeners( (listener)->{
//            ((GameViewEventListener) listener).move(evt);
//        });
//    }
//
//    public void emitEvent(BuildViewEvent evt){
//        executeEventListeners( (listener)->{
//            ((Controller) listener).build(evt);
//        });
//    }
//}

*/
