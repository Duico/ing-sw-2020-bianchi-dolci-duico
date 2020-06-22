package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;

import java.util.List;

/**
 * Represent event launched from Model when a Game that has been previously interrupted by a player disconnection
 * is trying to be resumed
 */
public class PersistencyEvent extends ModelEvent {
    private List<Player> players;
    private Board board;
    private TurnPhase turnPhase;

    /**
     * @param currentPlayer player who was able to make operations before game got interrupted
     * @param players players list of the game
     * @param board main board of the game
     * @param turnPhase turn phase where game got interrupted
     */
    public PersistencyEvent(Player currentPlayer, List<Player> players, Board board, TurnPhase turnPhase) {
        super(currentPlayer);
        this.players = players;
        this.board = board;
        this.turnPhase = turnPhase;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }


    @Override
    public void accept(ModelEventVisitor visitor) {
        visitor.visit(this);
    }
}
