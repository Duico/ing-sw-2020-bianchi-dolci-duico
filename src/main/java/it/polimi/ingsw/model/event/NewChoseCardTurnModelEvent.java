package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;

import java.util.ArrayList;
import java.util.List;

public class NewChoseCardTurnModelEvent extends NewTurnModelEvent {
    private List<String> cards;
    public NewChoseCardTurnModelEvent(Player player, TurnPhase turnPhase, List<String> cards) {
        super(player, turnPhase);
        this.cards=cards;
    }

    public List<String> getCards(){
        return cards;
    }
}
