package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the strategy that allows a Player's worker to build twice (not a dome)
 * in the same position
 */

public class MultipleBuild implements BuildStrategy {

    @Override
    public boolean isValidBuild(Position startPosition, Position destinationPosition, Boolean isDome, Board board) {
        try{
            BoardCell startCell = board.getBoardCell(startPosition);
            int numBuilds = startCell.getWorker().getNumBuilds();
            BuildStrategy buildStrategy = new DefaultBuild();
            if(numBuilds == 0){
                return buildStrategy.isValidBuild(startPosition, destinationPosition, isDome, board);
            }
            else if(numBuilds == 1){
                if(destinationPosition.equals(startCell.getWorker().getFirstBuild()))
                    return (buildStrategy.isValidBuild(startPosition, destinationPosition, isDome, board) && !isDome);
                else
                    return false;
            }
            else
                return false;
        } catch (NullPointerException e){
            throw new NullPointerException();
        }
    }

    @Override
    public boolean isAllowedToBuild(int numMoves, int numBuilds, Operation lastOperation) {
        return (numMoves == 1 && numBuilds >=0);
    }

    @Override
    public boolean isRequiredToBuild(int numMoves, int numBuilds, Operation lastOperation) {
        return numBuilds == 0;
    }
}
