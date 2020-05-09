package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.cli.ModelEventVisitor;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ChosenCardsModelEvent extends UpdateModelEvent {
    private List<String> chosenCards;
    public ChosenCardsModelEvent(Player player, List<String> chosenCards) {
        super(player);
        this.chosenCards=chosenCards;

    }

    public List<String> getChosenCards() {
        return chosenCards;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
