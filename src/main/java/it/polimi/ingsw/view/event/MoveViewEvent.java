package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Position;

public class MoveViewEvent extends ViewEvent {

    protected int workerId;
    protected Position destPosition;

    public MoveViewEvent(int workerId, Position destPosition) {
        this.workerId = workerId;
        this.destPosition = destPosition;
    }

    public Position destPosition(){
        return this.destPosition;
    }

    public int getWorkerId(){
        return this.workerId;
    }
}
