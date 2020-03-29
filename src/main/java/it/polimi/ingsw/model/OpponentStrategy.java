package it.polimi.ingsw.model;

public interface OpponentStrategy {
    /**
     *Check whether cell is empty or is occupied by a worker who can be pushed
     * @param worker Current worker
     * @param cell   Destination cell for current worker
     * @return False if cell is occupied by a worker who cannot be pushed away, True if empty or if opponent can be pushed
     */
    boolean isValidPush(Worker worker, BoardCell cell);

    /**
     * Doesn't do anything if cell is already empty, otherwise updates the position of the worker occupying the chosen cell
     *
     * @param worker
     * @param cell
     * @throws InvalidPushCell
     * @throws PositionOutOfBoundsException
     */
    void pushOpponent(Worker worker, BoardCell cell);

    Position destinationPosition(BoardCell startCell, BoardCell cell) throws InvalidPushCell, PositionOutOfBoundsException;

}