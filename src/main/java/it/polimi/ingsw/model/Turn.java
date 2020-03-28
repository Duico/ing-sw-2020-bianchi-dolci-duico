package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private Player currentPlayer;
    List<BoardCell> moves = new ArrayList<>();
    List<BoardCell> builds = new ArrayList<>();
    List<Operation> operations = new ArrayList<>();
    Board board;

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    protected Board getBoard() {
        return this.board;
    }

    public void addMovePosition(BoardCell cell){
        moves.add(cell);
        operations.add(Operation.MOVE);
    }

    public void addBuildCell(BoardCell cell) {
        builds.add(cell);
        operations.add(Operation.BUILD);
    }


}
