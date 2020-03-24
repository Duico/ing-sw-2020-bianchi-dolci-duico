package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

public class MoveSuccessModelEvent extends SuccessModelEvent {
    public MoveSuccessModelEvent(Player player) {
        super(player);
    }
}
