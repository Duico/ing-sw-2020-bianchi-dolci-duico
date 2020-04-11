package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;

public interface OpponentStrategy {
    /**
     *Check whether cell is empty or is occupied by a worker who can be pushed
     * @return False if cell is occupied by a worker who cannot be pushed away, True if empty or if opponent can be pushed
     */
    boolean isValidPush(Position startPosition, Position destPosition, boolean isOwnWorker, Board board);

    Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell, PositionOutOfBoundsException;


}