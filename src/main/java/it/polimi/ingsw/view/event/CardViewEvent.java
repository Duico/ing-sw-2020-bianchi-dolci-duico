
package it.polimi.ingsw.view.event;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

public class CardViewEvent extends GameViewEvent {
    private String cardName;

    public CardViewEvent(RemoteView view, String cardName){
        super(view);
        this.cardName=cardName;
    }
    public CardViewEvent(String cardName){
        this(null, cardName);
    }


    public String getCardName() {
        return cardName;
    }

    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }
}

