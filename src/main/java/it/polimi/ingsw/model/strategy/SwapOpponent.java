package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;

/**
 * Class that implements the power to move in an occupied cell and swap the opponent worker
 */

public class SwapOpponent implements OpponentStrategy {

    @Override
    public boolean isValidPush(Position startPosition, Position destPosition, boolean isOwnWorker, Board board) {
        if(isOwnWorker) return false;
        else return true;
    }


    @Override
    public Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell, PositionOutOfBoundsException {
        return startPosition;
    }
}
