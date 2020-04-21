package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Position;

import java.util.UUID;

public class BuildWorkerModelEvent extends WorkerModelEvent {
    private Position startPosition;
    private Position destinationPosition;
    public BuildWorkerModelEvent(UUID playerUUID, int workerId, Position startPosition, Position destinationPosition) {
        super(playerUUID, workerId);
        this.startPosition = startPosition;
        this.destinationPosition = destinationPosition;
    }
}
