package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class ChosenCardsModelEvent extends UpdateModelEvent {
    private List<String> cardDeck;
    private List<String> chosenCards;

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
