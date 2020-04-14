package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;

public class PlaceViewEvent extends ViewEvent{
    private Position destinstionPosition;

    public PlaceViewEvent(RemoteView view, Player player, Position destPosition) {
        super(view);
        this.destinstionPosition = destPosition;
    }

    public Position getDestinationPosition() {
        return destinstionPosition;
    }
}
