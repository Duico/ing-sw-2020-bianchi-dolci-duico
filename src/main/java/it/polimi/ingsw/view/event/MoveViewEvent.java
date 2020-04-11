package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

public class MoveViewEvent extends WorkerViewEvent {

    protected Position destPosition;

    public MoveViewEvent(Player player, int workerId, Position destPosition) {
        super(player, workerId);
        this.destPosition = destPosition;
    }

    public Position getDestinationPosition(){
        return this.destPosition;
    }


}
