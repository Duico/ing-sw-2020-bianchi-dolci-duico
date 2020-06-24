package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ViewEventVisitor;

import java.util.List;

/**
 * Represent message sent from client view to server when challenger player chooses Cards from deck
 */
public class ChallengerCardViewEvent extends GameViewEvent{
    List<String> cardNamesList;
    public ChallengerCardViewEvent(Player player, List<String> cardNamesList) {
        super(player);
        this.cardNamesList=cardNamesList;
    }
    public ChallengerCardViewEvent(List<String> cardNamesList){
        this(null, cardNamesList);
    }

    public List<String> getCardNamesList(){
        return this.cardNamesList;
    }

    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }
}
