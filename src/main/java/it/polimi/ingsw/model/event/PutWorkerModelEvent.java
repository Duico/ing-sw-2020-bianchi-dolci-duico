package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Position;

import java.util.UUID;

public class PutWorkerModelEvent extends WorkerModelEvent{
    private Position startPosition;
    private Position destinationPosition;
    private Position pushDestPosition;

    public PutWorkerModelEvent(UUID playerUUID, int workerId, Position startPosition, Position destinationPosition, Position pushDestPosition) {
        super(playerUUID, workerId);
        this.destinationPosition = destinationPosition;
        this.startPosition = startPosition;

    }
}
