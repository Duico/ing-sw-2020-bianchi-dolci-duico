package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

public class MoveWorkerModelEvent extends UpdateModelEvent {
    private Position startPosition;
    private Position destinationPosition;
    private Position pushPosition;

    public MoveWorkerModelEvent(Player player, Position startPosition, Position destinationPosition, Position pushPosition) {
        super(player);
        this.startPosition = startPosition;
        this.destinationPosition = destinationPosition;
        this.pushPosition = pushPosition;
    }

    public Position getStartPosition(){
        return startPosition;
    }

    public Position getDestinationPosition() {
        return destinationPosition;
    }

    public Position getPushPosition() {
        return pushPosition;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
