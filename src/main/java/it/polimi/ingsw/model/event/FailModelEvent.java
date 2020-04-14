package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

public class FailModelEvent extends ModelEvent {
    public FailModelEvent(Player player) {
        super(player.getUuid());
    }
}
