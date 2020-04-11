package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

public class DefaultBuild implements BuildStrategy {

    /**
     * Class that implements the default strategy of the BuildStrategy
     */

    @Override
    public boolean isValidBuild(Board board, Position startPosition, Position destinationPosition, Boolean isDome){
    try {
        BoardCell destinationCell = board.getBoardCell(destinationPosition);
        int dx = startPosition.getX() - destinationPosition.getX();
        int dy = startPosition.getY() - destinationPosition.getY();
        if (dx == 0 && dy == 0)
            return false;
        else if (dx < -1 || dx > 1 || dy < -1 || dy > 1)
            return false;
        else if (destinationCell.hasDome())
            return false;
        else if (destinationCell.getWorker() != null)
            return false;
        else if(destinationCell.getLevel().ordinal() != 3 && isDome)
            return false;
        else if(destinationCell.getLevel().ordinal() == 3 && !isDome)
            return false;
        else
            return true;
    }
        catch (NullPointerException e){
            throw new NullPointerException();
        }
    }


}
