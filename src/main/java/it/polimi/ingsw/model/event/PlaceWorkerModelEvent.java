package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

public class PlaceWorkerModelEvent extends WorkerModelEvent {
    private Position placePosition;
    public PlaceWorkerModelEvent(Player player, Position placePosition) {
        super(player, placePosition);
        this.placePosition = placePosition;
    }

    public Position getPlacePosition() {
        return placePosition;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
