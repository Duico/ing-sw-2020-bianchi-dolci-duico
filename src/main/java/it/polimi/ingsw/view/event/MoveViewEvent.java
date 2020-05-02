package it.polimi.ingsw.view.event;

import it.polimi.ingsw.message.OperationMessage;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.view.RemoteView;

public class MoveViewEvent extends WorkerViewEvent {
    private Position destinationPosition;

    public MoveViewEvent(RemoteView view, Position workerPosition, Position destinationPosition){
        super(view, workerPosition);
        this.destinationPosition= destinationPosition;
    }


    public Position getDestinationPosition() {
        return destinationPosition;
    }


}

