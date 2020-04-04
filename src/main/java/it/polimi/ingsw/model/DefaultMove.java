package it.polimi.ingsw.model;

/**
 * Class that implements the default strategy of the MoveStrategy
 */

public class DefaultMove implements MoveStrategy { //inherit functions from Interface

    @Override
    public boolean isValidMove(Position startPosition, Position destPosition, BoardCell[][] grid) {

        MoveStrategy pushStrategy = new PushMove();
        BoardCell destCell = grid[destPosition.getX()][destPosition.getY()];
        boolean isValidMove = pushStrategy.isValidMove(startPosition, destPosition, grid) && (destCell.getWorker() == null);
        return isValidMove;

    }


}
