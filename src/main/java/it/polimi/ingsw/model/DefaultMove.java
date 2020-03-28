package it.polimi.ingsw.model;

public class DefaultMove implements MoveStrategy { //inherit functions from Interface

    @Override
    public boolean isValidMove(Worker worker, BoardCell cell) {
        return false;
    }

    @Override
    public boolean isAllowedToMove(Worker worker) {
        return false;
    }

    @Override
    public boolean isRequiredToMove(Worker worker) {
        return false;
    }
}
