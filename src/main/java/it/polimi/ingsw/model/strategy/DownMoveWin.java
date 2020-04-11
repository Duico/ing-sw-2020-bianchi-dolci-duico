package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the strategy that allows a Player to win the game if a worker moves down two or more levels
 */

public class DownMoveWin implements WinStrategy {
    @Override
    public boolean isWinningMove(Position startPosition, Position destinationPosition, Board board){
        try{
            BoardCell startCell = board.getBoardCell(startPosition);
            BoardCell destinationCell = board.getBoardCell(destinationPosition);
            if(startCell.getLevel().ordinal() == 2 && destinationCell.getLevel().ordinal() == 0)
                return true;
            if(startCell.getLevel().ordinal() == 3 && (destinationCell.getLevel().ordinal() == 0 || destinationCell.getLevel().ordinal() == 1))
                return true;
            else
                return false;
        }
        catch (NullPointerException e){
            throw new NullPointerException();
        }
    }


}
