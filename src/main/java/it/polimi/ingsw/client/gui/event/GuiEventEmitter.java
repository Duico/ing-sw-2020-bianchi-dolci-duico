package it.polimi.ingsw.client.gui.event;

import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.model.Position;

public class GuiEventEmitter extends EventEmitter {
    private void executeGuiEventListeners(EventHandler<GuiEventListener> eventHandler){
        executeEventListeners(GuiEventListener.class, eventHandler);
    }
    public void makeMove(Position startPosition, Position destinationPosition){
        executeGuiEventListeners(listener -> listener.makeMove(startPosition, destinationPosition));
    }
    public void makeBuild(Position workerPosition, Position buildPosition, boolean isDome){
        executeGuiEventListeners(listener -> listener.makeBuild(workerPosition, buildPosition, isDome));
    }
    public void makePlace(Position workerPosition){
        executeGuiEventListeners(listener -> listener.makePlace(workerPosition) );
    }
    public void endTurn(){
        executeGuiEventListeners(listener -> listener.endTurn());
    }

}
