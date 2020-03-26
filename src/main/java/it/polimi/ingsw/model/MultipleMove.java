package it.polimi.ingsw.model;

public class MultipleMove implements MoveStrategy {
    public boolean isValidMove(BoardCell cell) {
        return false;
    }

    public boolean isAllowedToMove() {
        return false;
    }

    public boolean isRequiredToMove() {
        return false;
    }
}
