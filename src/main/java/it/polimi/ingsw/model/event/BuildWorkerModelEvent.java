package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

public class BuildWorkerModelEvent extends WorkerModelEvent {
    private Position destinationPosition;
    private boolean isDome;
    public BuildWorkerModelEvent(Player player, Position startPosition, Position destinationPosition, boolean isDome) {
        super(player, startPosition);
        this.destinationPosition = destinationPosition;
        this.isDome=isDome;
    }

    public Position getDestinationPosition() {
        return destinationPosition;
    }

    public boolean isDome() {
        return isDome;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
