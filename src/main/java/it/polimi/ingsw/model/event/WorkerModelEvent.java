package it.polimi.ingsw.model.event;

import java.util.UUID;

public class WorkerModelEvent extends ModelEvent{
    private int workerId;
    public WorkerModelEvent(UUID playerUUID, int workerId) {
        super(playerUUID);
        this.workerId = workerId;
    }
    public int getWorkerId() {
        return workerId;
    }
}
