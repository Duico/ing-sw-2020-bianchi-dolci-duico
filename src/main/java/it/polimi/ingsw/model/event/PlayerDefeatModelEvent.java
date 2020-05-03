package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

public class PlayerDefeatModelEvent extends UpdateModelEvent {
    private boolean isUndoAvailable;
    public PlayerDefeatModelEvent(Player player, boolean isUndoAvailable) {
        super(player);
        this.isUndoAvailable=isUndoAvailable;
    }
}
