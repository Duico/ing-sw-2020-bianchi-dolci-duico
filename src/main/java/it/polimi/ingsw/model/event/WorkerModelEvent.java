package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

/**
 * Represent generic event launched from Model when a worker makes an operation
 */
public abstract class WorkerModelEvent extends UpdateModelEvent{
    private Position startPosition;

    /**
     * @param player current player
     * @param startPosition position where player's worker is currently located
     */
    public WorkerModelEvent(Player player, Position startPosition) {
        super(player);
        this.startPosition = startPosition;
    }
    public Position getStartPosition(){
        return this.startPosition;
    }

    public abstract void accept(ModelEventVisitor visitor);

}
