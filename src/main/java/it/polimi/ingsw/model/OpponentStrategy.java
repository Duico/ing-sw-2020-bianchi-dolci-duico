package it.polimi.ingsw.model;

public interface OpponentStrategy {
    /**
     *Check whether cell is empty or is occupied by a worker who can be pushed
     * @return False if cell is occupied by a worker who cannot be pushed away, True if empty or if opponent can be pushed
     */
    boolean isValidPush (Position startPosition, Position destPosition, BoardCell[][] grid);

    Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell, PositionOutOfBoundsException;


}