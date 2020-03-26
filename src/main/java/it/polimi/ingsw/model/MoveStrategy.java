package it.polimi.ingsw.model;

public interface MoveStrategy {
    boolean isValidMove(BoardCell cell);
    boolean isAllowedToMove();
    boolean isRequiredToMove();

}
