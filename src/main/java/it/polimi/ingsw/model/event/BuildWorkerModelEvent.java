package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

import java.util.UUID;

public class BuildWorkerModelEvent extends WorkerModelEvent {
    private Position destinationPosition;
    public BuildWorkerModelEvent(Player player, Position startPosition, Position destinationPosition) {
        super(player, startPosition);
        this.destinationPosition = destinationPosition;
    }

    public Position getDestinationPosition() {
        return destinationPosition;
    }
}
