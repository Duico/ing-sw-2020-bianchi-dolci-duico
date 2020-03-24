package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Position;

import java.util.UUID;

public class PlaceWorkerModelEvent extends WorkerModelEvent {
    private Position placePosition;
    public PlaceWorkerModelEvent(UUID playerUUID, int workerId, Position placePosition) {
        super(playerUUID, workerId);
        this.placePosition = placePosition;
    }
}
