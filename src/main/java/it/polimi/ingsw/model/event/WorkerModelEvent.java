package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

public class WorkerModelEvent extends UpdateModelEvent{
    private Position startPosition;
    public WorkerModelEvent(Player player, Position startPosition) {
        super(player);
        this.startPosition = startPosition;
    }
    public Position getStartPosition(){
        return this.startPosition;
    }
}
