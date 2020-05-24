package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;

public class SetCardModelEvent extends UpdateModelEvent {
    //private String cardName;
    private Card card;
    public SetCardModelEvent(Player player, Card card) {
        super(player);
        this.card = card;
    }

    public Card getCard(){
        return this.card;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
