package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the power to deny an up move to other players in the next turn if current player
 * moves up this turn
 */
public class UpMoveBlock implements BlockStrategy {
    @Override
    public boolean isBlockMove(Position startPosition, Position destinationPosition, Board board) {
        BoardCell startCell = board.getBoardCell(startPosition);
        BoardCell destCell = board.getBoardCell(destinationPosition);
        if (startCell.getLevel().getOrd()<destCell.getLevel().getOrd())
            return true;
        else
            return false;

    }

    public boolean blockNextPlayer(Position startPosition, Position destinationPosition, Board board){
        BoardCell startCell = board.getBoardCell(startPosition);
        BoardCell destCell = board.getBoardCell(destinationPosition);
        if (startCell.getLevel().getOrd()<destCell.getLevel().getOrd())
            return true;
        else
            return false;
    }
}
