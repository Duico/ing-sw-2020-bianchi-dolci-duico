
package it.polimi.ingsw.view.event;
import it.polimi.ingsw.view.RemoteView;

public class CardViewEvent extends GameViewEvent {
    private String cardName;

    public CardViewEvent(RemoteView view, String cardName){
        super(view);
        this.cardName=cardName;
    }


    public String getCardName() {
        return cardName;
    }
}

