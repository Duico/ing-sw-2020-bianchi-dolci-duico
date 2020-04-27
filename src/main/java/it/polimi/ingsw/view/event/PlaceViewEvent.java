package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;

public class PlaceViewEvent extends WorkerViewEvent{
    private Position destinationPosition;

    public PlaceViewEvent(RemoteView view, Player player, Position placePosition) {
        super(view, placePosition);
        this.destinationPosition = placePosition;
    }

    public Position getDestinationPosition() {
        return destinationPosition;
    }
}
