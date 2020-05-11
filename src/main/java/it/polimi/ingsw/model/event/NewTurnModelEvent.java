package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.cli.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;

import java.util.ArrayList;
import java.util.List;

public class NewTurnModelEvent extends UpdateModelEvent {
    private TurnPhase turnPhase;
    private List<Player> players;
    //private List<String> cardDeck;

    public NewTurnModelEvent(Player player, TurnPhase turnPhase, List<Player> players) {
        super(player);
        this.turnPhase = turnPhase;
        this.players=players;

    }

    /*public NewTurnModelEvent(Player player, TurnPhase turnPhase, List<Player> players){
        this(player, turnPhase, players, null);
    }*/

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /*public List<String> getCardDeck(){
        return cardDeck;
    }*/

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }

}
