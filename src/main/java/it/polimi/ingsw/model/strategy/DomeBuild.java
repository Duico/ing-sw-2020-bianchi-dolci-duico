package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the strategy that allows a Player's worker to build a dome at any level
 */

public class DomeBuild implements BuildStrategy {

    @Override
    public boolean isValidBuild(Position startPosition, Position destinationPosition, Boolean isDome, Board board) {
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
            else if(destinationCell.getLevel().getOrd() == 3 && !isDome)
                return false;
            else
                return true;
        }
        catch (NullPointerException e){
            throw new NullPointerException();
        }
    }

    @Override
    public boolean isAllowedToBuild(int numMoves, int numBuilds, Operation lastOperation) {
        return (numMoves == 1 && numBuilds ==0);
    }

    @Override
    public boolean isRequiredToBuild(int numMoves, int numBuilds, Operation lastOperation) {
        return numBuilds == 0;
    }
}
