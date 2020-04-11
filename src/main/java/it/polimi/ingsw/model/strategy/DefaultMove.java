package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the default strategy of the MoveStrategy
 */

public class DefaultMove implements MoveStrategy { //inherit functions from Interface

    @Override
    public boolean isValidMove(Position startPosition, Position destPosition, Board board) {

        MoveStrategy pushStrategy = new PushMove();
        BoardCell destCell = board.getBoardCell(destPosition);
        boolean isValidMove = pushStrategy.isValidMove(startPosition, destPosition, board) && (destCell.getWorker() == null);
        return isValidMove;

    }


}
