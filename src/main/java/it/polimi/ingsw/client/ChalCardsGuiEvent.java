package it.polimi.ingsw.client;

import java.util.ArrayList;

public class ChalCardsGuiEvent {
    private ArrayList<String> cards;

    public ChalCardsGuiEvent(ArrayList<String> cards){
        this.cards=cards;
    }

    public ArrayList<String> getCards() {
        return cards;
    }
}
