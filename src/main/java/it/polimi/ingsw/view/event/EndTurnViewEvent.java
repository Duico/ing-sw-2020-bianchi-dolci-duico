package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;

public class EndTurnViewEvent extends ViewEvent{

    public EndTurnViewEvent(Player player) {
        super(player);
    }
}
