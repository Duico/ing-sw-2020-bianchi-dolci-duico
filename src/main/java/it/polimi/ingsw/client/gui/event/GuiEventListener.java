package it.polimi.ingsw.client.gui.event;

import it.polimi.ingsw.model.Position;

import java.util.EventListener;

public interface GuiEventListener extends EventListener {
    void makeMove(Position startPosition, Position destPosition);
    void makeBuild(Position workerPosition, Position buildPosition, boolean isDome);
    void makePlace(Position workerPosition);
    void endTurn();
}
