package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;

/**
 * Represent event launched from Model when a player wins the game
 */
public class WinModelEvent extends ModelEvent {

    /**
     * @param player player that wins the game
     */
    public WinModelEvent(Player player) {
        super(player);
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
