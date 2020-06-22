package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;

/**
 * Represent event launched from Model when a player is defeated
 */
public class PlayerDefeatModelEvent extends UpdateModelEvent {
    private boolean isUndoAvailable;

    /**
     * @param player player who is defeated
     * @param isUndoAvailable true if player is still allowed to undo his last operation
     */
    public PlayerDefeatModelEvent(Player player, boolean isUndoAvailable) {
        super(player);
        this.isUndoAvailable=isUndoAvailable;
    }


    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }


}
