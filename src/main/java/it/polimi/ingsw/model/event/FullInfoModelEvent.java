package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.cli.ModelEventVisitor;
import it.polimi.ingsw.message.InfoType;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardDeck;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class FullInfoModelEvent extends ModelEvent {
    private InfoType type;
    private Board board;
    private ArrayList<Player> players;

    public FullInfoModelEvent(InfoType type, Player currentPlayer, ArrayList<Player> players, Board board) {
        super(currentPlayer);
        this.type=type;
        this.players = players;
        this.board = board;

    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public InfoType getType() {
        return type;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
