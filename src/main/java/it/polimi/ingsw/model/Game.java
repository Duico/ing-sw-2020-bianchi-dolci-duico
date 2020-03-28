package it.polimi.ingsw.model;

public class Game {
    private Turn turn;
    private Turn previousTurn;

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public Turn getPreviousTurn() {
        return previousTurn;
    }

    public void setPreviousTurn(Turn previousTurn) {
        this.previousTurn = previousTurn;
    }

}