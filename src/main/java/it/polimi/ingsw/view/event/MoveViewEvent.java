package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

public class MoveViewEvent extends WorkerViewEvent {
    private Position destinationPosition;

    public MoveViewEvent(Player player, Position workerPosition, Position destinationPosition){
        super(player, workerPosition);
        this.destinationPosition= destinationPosition;
    }
    public MoveViewEvent(Position workerPosition, Position destinationPosition){
        this(null, workerPosition, destinationPosition);
    }


    public Position getDestinationPosition() {
        return destinationPosition;
    }

    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }


}

