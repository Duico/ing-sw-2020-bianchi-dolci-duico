package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;

public class CardsChallengerViewEvent extends ViewEvent {
    ArrayList<String> Cards = new ArrayList<>();
    public CardsChallengerViewEvent(RemoteView view) {
        super(view);
    }

    public ArrayList<String> getCards(){
        return this.Cards;
    }
}
