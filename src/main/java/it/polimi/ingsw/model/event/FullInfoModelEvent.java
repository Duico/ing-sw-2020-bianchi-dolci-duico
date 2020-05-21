package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class FullInfoModelEvent extends ModelEvent {
    private InfoType type;
    private Board board;
    private List<Player> players;

    public FullInfoModelEvent(InfoType type, Player currentPlayer, List<Player> players, Board board) {
        super(currentPlayer);
        this.type=type;
        this.players = players;
        this.board = board;

    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public InfoType getType() {
        return type;
    }

    @Override
    public void accept(ModelEventVisitor visitor){
        visitor.visit(this);
    }

    public enum InfoType{
        UNDO,
        INIT_GAME,
        PERSISTENCY
    }
}
