package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;

/**
 * Represent generic event launched from Model when any kind of game or player's property changes
 */
public abstract class UpdateModelEvent extends ModelEvent {
    /**
     * @param player player who gets updated
     */
    public UpdateModelEvent(Player player) {
        super(player);
    }

    public abstract void accept(ModelEventVisitor visitor);

}
