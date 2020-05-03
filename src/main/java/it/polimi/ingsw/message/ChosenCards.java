package it.polimi.ingsw.message;

import java.util.ArrayList;

public class ChosenCards {
    private ArrayList<String> cards;
    public ChosenCards(ArrayList<String> cards){
        this.cards= cards;
    }

    public ArrayList<String> getCards() {
        return cards;
    }
}
