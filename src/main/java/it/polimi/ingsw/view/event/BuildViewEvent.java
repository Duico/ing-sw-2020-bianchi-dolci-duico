package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;


public class BuildViewEvent extends WorkerViewEvent {
    private Position destinationPosition;
    private boolean isDome;

    public BuildViewEvent(RemoteView view, Player player, Position workerPosition, Position destinationPosition, boolean isDome) {
        super(view, workerPosition);
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
