package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;

import java.util.List;

/**
 * Represent event launched from Model when any kind of game Turn ends
 */
public class NewTurnModelEvent extends UpdateModelEvent {
    private TurnPhase turnPhase;
    private List<Player> players;

    /**
     * @param player current player that end his turn
     * @param turnPhase current turn phase
     * @param players list of all players
     */
    public NewTurnModelEvent(Player player, TurnPhase turnPhase, List<Player> players) {
        super(player);
        this.turnPhase = turnPhase;
        this.players=players;

    }


    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public List<Player> getPlayers() {
        return players;
    }


    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }

}
