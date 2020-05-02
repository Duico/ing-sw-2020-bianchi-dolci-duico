package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the default strategy of the WinStrategy
 */

public class DefaultWin implements WinStrategy {

    @Override
    public boolean isWinningMove(Position startPosition, Position destinationPosition, Board board) {
        try{
            BoardCell startCell = board.getBoardCell(startPosition);
            BoardCell destinationCell = board.getBoardCell(destinationPosition);
            if(startCell.getLevel().getOrd() == 2 && destinationCell.getLevel().getOrd() == 3 && !destinationCell.hasDome() && destinationCell.getWorker() == null) //FIX should not check if move is valid, only if it is winning*/
                return true;
            else
                return false;
        }
        catch (NullPointerException e){
            throw new NullPointerException();
        }
    }

}
