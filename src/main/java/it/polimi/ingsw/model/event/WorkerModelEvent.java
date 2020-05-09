package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.cli.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

public abstract class WorkerModelEvent extends UpdateModelEvent{
    private Position startPosition;
    public WorkerModelEvent(Player player, Position startPosition) {
        super(player);
        this.startPosition = startPosition;
    }
    public Position getStartPosition(){
        return this.startPosition;
    }

    public abstract void accept(ModelEventVisitor visitor);

}
