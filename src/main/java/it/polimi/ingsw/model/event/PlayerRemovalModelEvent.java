package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

public class PlayerRemovalModelEvent extends UpdateModelEvent {
    public PlayerRemovalModelEvent(Player player) {
        super(player);
    }
}
