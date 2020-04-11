package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

public class PlaceViewEvent extends ViewEvent{
    private Position destinstionPosition;

    public PlaceViewEvent(Player player, Position destPosition) {
        super(player);
        this.destinstionPosition = destPosition;
    }

    public Position getDestinationPosition() {
        return destinstionPosition;
    }
}
