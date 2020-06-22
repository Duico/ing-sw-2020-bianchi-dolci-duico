package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;

/**
 * Represent event launched from Model when a generic operation done by a player fails
 */
public class FailModelEvent extends ModelEvent {

    /**
     * @param player current player
     */
    public FailModelEvent(Player player) {
        super(player);
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
