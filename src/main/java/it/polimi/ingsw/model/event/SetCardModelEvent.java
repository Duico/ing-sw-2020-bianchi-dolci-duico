package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;

public class SetCardModelEvent extends UpdateModelEvent {
    private String cardName;
    public SetCardModelEvent(Player player, String cardName) {
        super(player);
        this.cardName = cardName;
    }

    public String getCardName(){
        return this.cardName;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
