package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

/**
 * Represent event launched from Model when a worker moves from a position to another
 */
public class MoveWorkerModelEvent extends UpdateModelEvent {
    private Position startPosition;
    private Position destinationPosition;
    private Position pushPosition;

    /**
     * @param player current player that is moving his worker
     * @param startPosition position where the player's worker is currently located
     * @param destinationPosition position where the player's worker is moving to
     * @param pushPosition position where the worker already located in destination position is going to be pushed
     */
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
