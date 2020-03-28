package it.polimi.ingsw.model;

public class BuildAtFirstMove implements MoveStrategy {
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
