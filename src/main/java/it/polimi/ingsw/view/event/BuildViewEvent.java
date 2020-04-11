package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;


public class BuildViewEvent extends WorkerViewEvent {
    private Position destinationPosition;
    private boolean isDome;

    public BuildViewEvent(Player player, int workerId, Position destinationPosition, boolean isDome) {
        super(player, workerId);
        this.destinationPosition = destinationPosition;
        this.isDome = isDome;
    }

    public Position getDestinationPosition() {
        return destinationPosition;
    }

    public boolean isDome() {
        return isDome;
    }
}
