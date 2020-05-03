package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;

import java.util.ArrayList;

public class NewTurnModelEvent extends UpdateModelEvent {
    private TurnPhase turnPhase;
    public NewTurnModelEvent(Player player, TurnPhase turnPhase) {
        super(player);
        this.turnPhase = turnPhase;
    }
}
