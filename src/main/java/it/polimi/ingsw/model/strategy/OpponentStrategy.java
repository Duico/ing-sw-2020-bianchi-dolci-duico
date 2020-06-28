package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.InvalidPushCell;

import java.io.Serializable;

public interface OpponentStrategy extends Serializable {
    /**
     * Check whether cell is empty or is occupied by a worker who can be pushed
     * @param startPosition position of the worker who's going to be pushed
     * @param destPosition destination position of push
     * @param isOwnWorker true if selected worker is one of current player's workers
     * @param board main board of the game
     * @return False if cell is occupied by a worker who cannot be pushed away, True if empty or if opponent can be pushed
     */
    boolean isValidPush(Position startPosition, Position destPosition, boolean isOwnWorker, Board board);

    /**
     * return the push position based on the start and the destination position
     * @param startPosition start position of the movement
     * @param destPosition destination position of the movement
     * @return push position
     * @throws InvalidPushCell thrown if push cell is not valid
     */
    Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell;


}