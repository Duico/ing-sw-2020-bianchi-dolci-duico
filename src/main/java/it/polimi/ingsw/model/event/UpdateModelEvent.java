package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

public class UpdateModelEvent extends ModelEvent {
    public UpdateModelEvent(Player player) {
        super(player);
    }
}
