package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

public class SuccessModelEvent extends ModelEvent {
    public SuccessModelEvent(Player player) {
        super(player.getUuid());
    }
}
