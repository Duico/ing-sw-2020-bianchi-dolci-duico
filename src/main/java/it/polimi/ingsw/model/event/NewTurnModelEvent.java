package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.cli.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;

import java.util.ArrayList;
import java.util.List;

public class NewTurnModelEvent extends UpdateModelEvent {
    private TurnPhase turnPhase;
    private ArrayList<String> players;
    private List<String> cardDeck;

    public NewTurnModelEvent(Player player, TurnPhase turnPhase, ArrayList<String> players, List<String> cardDeck) {
        super(player);
        this.turnPhase = turnPhase;
        this.players=players;
        this.cardDeck=cardDeck;
    }

    public NewTurnModelEvent(Player player, TurnPhase turnPhase, ArrayList<String> players){
        this(player, turnPhase, players, null);
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public List<String> getCardDeck(){
        return cardDeck;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }

}
