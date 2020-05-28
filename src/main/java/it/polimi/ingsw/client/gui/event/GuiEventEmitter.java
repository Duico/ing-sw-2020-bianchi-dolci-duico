package it.polimi.ingsw.client.gui.event;

import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.model.Position;

import java.util.List;

public class GuiEventEmitter extends EventEmitter {
    private void executeGuiEventListeners(EventHandler<GuiEventListener> eventHandler){
        executeEventListeners(GuiEventListener.class, eventHandler);
    }
    public void emitMove(Position startPosition, Position destinationPosition){
        executeGuiEventListeners(listener -> listener.onMove(startPosition, destinationPosition));
    }
    public void emitBuild(Position workerPosition, Position buildPosition, boolean isDome){
        executeGuiEventListeners(listener -> listener.onBuild(workerPosition, buildPosition, isDome));
    }

    public void emitEndTurn(){
        executeGuiEventListeners(listener -> listener.onEndTurn());
    }

    public void emitLogin(String username, Integer numPlayers){
        executeGuiEventListeners(listener -> listener.onLogin(username, numPlayers));
    }

    public void emitChosenCard(String chosenCard){
        executeGuiEventListeners(listener -> listener.onChooseCard(chosenCard));
    }

    public void emitChallengerCards(List<String> challengerCards){
        executeGuiEventListeners(listener -> listener.onChallengeCards(challengerCards));
    }

    public void emitFirstPlayer(String firstPlayer){
        executeGuiEventListeners(listener -> listener.onFirstPlayer(firstPlayer));
    }

    public void emitPlaceWorker(Position position){
        executeGuiEventListeners(listener -> listener.onPlaceWorker(position));
    }

}
