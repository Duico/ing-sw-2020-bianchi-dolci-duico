package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;

public class CardViewEvent extends GameViewEvent {
    private String card;

    public CardViewEvent(RemoteView view, String card) {
        super(view);
        this.card= card;
    }

    public String getCardName(){
        return this.card;
    }
}
