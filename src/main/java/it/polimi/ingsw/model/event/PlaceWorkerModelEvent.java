package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

public class PlaceWorkerModelEvent extends WorkerModelEvent {
    public PlaceWorkerModelEvent(Player player, Position placePosition) {
        super(player, placePosition);
    }

    public Position getPlacePosition() {
        return super.getStartPosition();
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
