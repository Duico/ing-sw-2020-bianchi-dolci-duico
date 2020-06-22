package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

/**
 * Represent event launched from Model when a player places one of his workers on the Board
 */
public class PlaceWorkerModelEvent extends WorkerModelEvent {
    /**
     * @param player current player who is placing his worker
     * @param placePosition position where the worker is placed
     */
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
