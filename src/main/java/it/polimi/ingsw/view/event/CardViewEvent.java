
package it.polimi.ingsw.view.event;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ViewEventVisitor;

/**
 * Represent message sent from client view to server when the player chooses one Card to play with
 */
public class CardViewEvent extends GameViewEvent {
    private String cardName;

    public CardViewEvent(Player player, String cardName){
        super(player);
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

