package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;

/**
 * Class that implements the OpponentStrategy interface. Describe the default opponent worker strategy
 */

public class DefaultOpponent implements OpponentStrategy {

    @Override
    public boolean isValidPush(Position startPosition, Position destPosition, boolean isOwnWorker, Board board) {
        if(board.getBoardCell(destPosition).getWorker() != null)
            return false;
        else
            return true;
    }


    @Override
    public Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell {
        return null;
    }
}
