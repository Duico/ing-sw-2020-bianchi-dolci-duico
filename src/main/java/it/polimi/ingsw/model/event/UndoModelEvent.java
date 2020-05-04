package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public class UndoModelEvent extends UpdateModelEvent {
    private Board board;
    private ArrayList<Player> players;

    public UndoModelEvent(Player player, Board board, ArrayList<Player> players) {
        super(player);
        this.board=board;
        this.players=players;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
