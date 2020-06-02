package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.event.Event;
import it.polimi.ingsw.model.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class PersistencyEvent extends ModelEvent {
    private List<Player> players;
    private Player currentPlayer;
    private Board board;
    private TurnPhase turnPhase;

    public PersistencyEvent(List<Player> players, Player currentPlayer, Board board, TurnPhase turnPhase) {
        //myPlayer, set later
        super(null);
        this.currentPlayer = currentPlayer;
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

    }
}
