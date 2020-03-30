package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private Player currentPlayer;
    private Worker currentWorker;
    private ArrayList<BoardCell> moves;
    private ArrayList<BoardCell> builds;
    private ArrayList<Operation> operations;
    private BoardCell initialCell;
    Board board; // ask if is useful

    public Turn(Player currentPlayer){
        this.currentPlayer = currentPlayer;
        moves = new ArrayList<>();
        builds = new ArrayList<>();
        operations = new ArrayList<>();
    }

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

    public List<BoardCell> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<BoardCell> moves) {
        this.moves = moves;
    }

    public List<BoardCell> getBuilds() {
        return builds;
    }

    public void setBuilds(ArrayList<BoardCell> builds) {
        this.builds = builds;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    public BoardCell getInitialCell() {
        return initialCell;
    }

    public void setInitialCell(BoardCell initialCell) {
        this.initialCell = initialCell;
    }

    public int getNumMoves(){
        return this.moves.size();
    }

    public int getNumBuilds(){
        return this.builds.size();
    }

    public BoardCell getLastMove(){
        return moves.get(moves.size()-1);
    }
}
