package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.event.Event;
import it.polimi.ingsw.model.*;

import java.util.List;

public class PersistencyEvent extends ModelEvent {
    private List<Player> players;
    private Board board;
    private TurnPhase turnPhase;

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
