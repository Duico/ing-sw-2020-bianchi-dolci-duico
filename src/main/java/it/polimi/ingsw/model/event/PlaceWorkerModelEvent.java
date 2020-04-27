package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

import java.util.UUID;

public class PlaceWorkerModelEvent extends WorkerModelEvent {
    private Position placePosition;
    public PlaceWorkerModelEvent(Player player, Position placePosition) {
        super(player, placePosition);
        this.placePosition = placePosition;
    }

    public Position getPlacePosition() {
        return placePosition;
    }
}
