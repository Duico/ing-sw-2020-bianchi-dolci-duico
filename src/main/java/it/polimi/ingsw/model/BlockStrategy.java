package it.polimi.ingsw.model;

public interface BlockStrategy {
    boolean isValidMoveForNextPlayer(Worker worker, BoardCell cell);
}
