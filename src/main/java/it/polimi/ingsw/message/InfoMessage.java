package it.polimi.ingsw.message;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import java.io.Serializable;
import java.util.ArrayList;

public class InfoMessage implements Serializable {
    private InfoType type;
    private ArrayList<Player> players;
    private Board board;

    public InfoMessage(InfoType type, ArrayList<Player> players, Board board){
        this.type=type;
        this.players=players;
        this.board=board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }
}
