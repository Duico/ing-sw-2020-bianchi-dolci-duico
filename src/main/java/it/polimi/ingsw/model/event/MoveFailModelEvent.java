package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

public class MoveFailModelEvent extends FailModelEvent {
    public MoveFailModelEvent(Player player) {
        super(player);
    }
}
