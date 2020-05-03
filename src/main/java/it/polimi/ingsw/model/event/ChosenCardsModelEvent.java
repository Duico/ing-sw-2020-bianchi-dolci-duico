package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ChosenCardsModelEvent extends UpdateModelEvent {
    private ArrayList<String> chosenCards;
    public ChosenCardsModelEvent(Player player, ArrayList<String> chosenCards) {
        super(player);
        this.chosenCards=chosenCards;

    }

    public List<String> getChosenCards() {
        return chosenCards;
    }
}
