package it.polimi.ingsw.model;

/**
 * Class that implements the OpponentStrategy interface. Describe the default opponent worker strategy
 */

public class DefaultOpponent implements OpponentStrategy {

    @Override
    public boolean isValidPush(Position startPosition, Position destPosition, BoardCell[][] grid) {
        if(grid[destPosition.getX()][destPosition.getY()].getWorker() != null)
            return false;
        else
            return true;
    }


    @Override
    public Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell, PositionOutOfBoundsException {
        return null;
    }
}
