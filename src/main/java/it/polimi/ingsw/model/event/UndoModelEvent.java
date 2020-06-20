package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class UndoModelEvent extends UpdateModelEvent {
    private Board board;
    private List<Player> players;

    public UndoModelEvent(Player player, Board board, List<Player> players) {
        super(player);
        this.board=board;
        this.players=players;
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }
}
