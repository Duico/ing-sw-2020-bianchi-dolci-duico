package it.polimi.ingsw.model;

public class DefaultOpponent implements OpponentStrategy {

    @Override
    public boolean isValidPush(Worker worker, BoardCell cell) {
        return false;
    }

    @Override
    public void pushOpponent(Worker worker, BoardCell cell) throws InvalidPushCell, PositionOutOfBoundsException {

    }

    @Override
    public Position destinationPosition(BoardCell startCell, BoardCell cell) throws InvalidPushCell, PositionOutOfBoundsException {
        return null;
    }
}
