package it.polimi.ingsw.model;

public class Worker {
    private BoardCell position;
    private Turn turn;
    private Player player;

    public Worker(){

    }

    public BoardCell getCell() {
        return position;
    }

    public void setCell(BoardCell cell) {
        this.position = cell;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void Move(BoardCell cell){

    }

    public void Build(BoardCell cell){

    }

    public void ForceMove(BoardCell cell){

    }
}
