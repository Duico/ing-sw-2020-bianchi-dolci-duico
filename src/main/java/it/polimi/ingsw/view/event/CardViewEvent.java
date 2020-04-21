package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;

public class CardViewEvent extends ViewEvent {
    private String card;

    public CardViewEvent(RemoteView view, String card) {
        super(view);
        this.card= card;
    }

    public String getNameCard(){
        return this.card;
    }
}
