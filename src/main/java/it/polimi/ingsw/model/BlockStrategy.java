package it.polimi.ingsw.model;

import java.util.ArrayList;


public interface BlockStrategy {
    /**
     * TODO
     * @param startPosition  TODO
     * @param destinationPosition
     * @param previousPositions
     * @return True if movement from startPosition to destinationPosition is not blocked by the BlockStrategy of the previous worker
     */
    boolean isValidMoveForNextPlayer(Position startPosition, Position destinationPosition, ArrayList<Position> previousPositions);
}
