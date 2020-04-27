package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ChosenCardsModelEvent extends UpdateModelEvent {
    private List<String> chosenCards;
    public ChosenCardsModelEvent(Player player, List<String> chosenCards) {
        super(player);

    }

    public List<String> getChosenCards() {
        return chosenCards;
    }
}
