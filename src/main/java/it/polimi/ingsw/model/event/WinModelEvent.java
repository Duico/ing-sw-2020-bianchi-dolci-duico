package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

public class WinModelEvent extends ModelEvent {
    public WinModelEvent(Player player) {
        super(player);
    }
}
