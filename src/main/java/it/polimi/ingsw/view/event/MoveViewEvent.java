package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;

public class MoveViewEvent extends WorkerViewEvent {

    protected Position destPosition;

    public MoveViewEvent(RemoteView view, int workerId, Position destPosition) {
        super(view, workerId);
        this.destPosition = destPosition;
    }

    public Position getDestinationPosition(){
        return this.destPosition;
    }


}
