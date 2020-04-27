package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;

public class MoveViewEvent extends WorkerViewEvent {
    private Position destPosition;

    public MoveViewEvent(RemoteView view, Position startPosition, Position destPosition) {
        super(view, startPosition);
        this.destPosition = destPosition;
    }

    public Position getDestinationPosition(){
        return this.destPosition;
    }


}
