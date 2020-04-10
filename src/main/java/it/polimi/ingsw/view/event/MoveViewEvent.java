package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

public class MoveViewEvent extends ViewEvent {

    protected int workerId;
    protected Position destPosition;

    public MoveViewEvent(Player player, int workerId, Position destPosition) {
        super(player);
        this.workerId = workerId;
        this.destPosition = destPosition;
    }

    public Position getDestPosition(){
        return this.destPosition;
    }

    public int getWorkerId(){
        return this.workerId;
    }
}
