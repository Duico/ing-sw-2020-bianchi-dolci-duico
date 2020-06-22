package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

/**
 * Represent event launched from Model when a worker builds on the board
 */
public class BuildWorkerModelEvent extends WorkerModelEvent {
    private Position destinationPosition;
    private boolean isDome;

    /**
     * @param player player who has built
     * @param startPosition position where player's worker is currently located
     * @param destinationPosition building position
     * @param isDome True if player has built a dome in destination position
     */
    public BuildWorkerModelEvent(Player player, Position startPosition, Position destinationPosition, boolean isDome) {
        super(player, startPosition);
        this.destinationPosition = destinationPosition;
        this.isDome=isDome;
    }

    public Position getDestinationPosition() {
        return destinationPosition;
    }

    public boolean isDome() {
        return isDome;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
