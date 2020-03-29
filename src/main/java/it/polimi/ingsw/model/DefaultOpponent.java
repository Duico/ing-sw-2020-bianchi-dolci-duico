package it.polimi.ingsw.model;

/**
 * Class that implements the OpponentStrategy interface. Describe the default opponent worker strategy
 */

public class DefaultOpponent implements OpponentStrategy {

    @Override
    public boolean isValidPush(Worker worker, BoardCell cell) {
        if(cell.getWorker() != null)
            return false;
        else
            return true;
    }


    @Override
    public void pushOpponent(Worker worker, BoardCell cell) {

    }

    @Override
    public Position destinationPosition(BoardCell startCell, BoardCell cell) throws InvalidPushCell, PositionOutOfBoundsException {
        return null;
    }
}
