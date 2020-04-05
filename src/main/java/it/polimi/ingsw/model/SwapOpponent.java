package it.polimi.ingsw.model;

/**
 * Class that implements the power to move in an occupied cell and swap the opponent worker
 */

public class SwapOpponent implements OpponentStrategy {

    @Override
    public boolean isValidPush(Position startPosition, Position destPosition, boolean isOwnWorker, BoardCell[][] grid) {
        if(isOwnWorker) return false;
        else return true;
    }


    @Override
    public Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell, PositionOutOfBoundsException {
        return startPosition;
    }
}
