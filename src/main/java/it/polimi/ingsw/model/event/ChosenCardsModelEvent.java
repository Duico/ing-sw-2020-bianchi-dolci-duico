package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * Represent event launched from Model when challenger player chooses cards to play with
 */
public class ChosenCardsModelEvent extends UpdateModelEvent {
    private List<String> cardDeck;
    private List<String> chosenCards;

    /**
     * @param player player who chooses the cards
     * @param cardDeck list of card from which the player can choose
     * @param chosenCards list of chosen cards
     */
    public ChosenCardsModelEvent(Player player, List<String> cardDeck, List<String> chosenCards) {
        super(player);
        this.cardDeck = cardDeck;
        this.chosenCards=chosenCards;

    }

    public List<String> getChosenCards() {
        return chosenCards;
    }

    public List<String> getCardDeck() {
        return cardDeck;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
