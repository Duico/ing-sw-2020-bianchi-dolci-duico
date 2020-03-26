package it.polimi.ingsw.model;

public interface BlockStrategy {
    boolean isValidMoveForNextPlayer(BoardCell cell);
}
