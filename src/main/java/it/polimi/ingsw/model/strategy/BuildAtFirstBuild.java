package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the strategy that allows a Player's worker to decide if 'Build' before 'Move'
 * and then follow the sequence Build - Move - Build instead of the default sequence Move - Build
 */

public class BuildAtFirstBuild implements BuildStrategy {

    @Override
    public boolean isValidBuild(Position startPosition, Position destinationPosition, Boolean isDome, Board board)
    {
        BuildStrategy buildAtFirstBuild = new DefaultBuild();
        return buildAtFirstBuild.isValidBuild(startPosition, destinationPosition, isDome, board);
    }


    @Override
    public boolean isAllowedToBuild(int numMoves, int numBuilds, Operation lastOperation) {
        if(numBuilds == 0 && numMoves == 0)
            return true;
        else if(numMoves == 1 && numBuilds == 0)
            return true;
        else if(numMoves == 1 && numBuilds == 1 && lastOperation.equals(Operation.MOVE))
            return true;
        else
            return false;

    }

    @Override
    public boolean isRequiredToBuild(int numMoves, int numBuilds, Operation lastOperation) {
        if(numBuilds == 0)
            return true;
        if(numBuilds == 1 && numMoves == 1 && lastOperation.equals(Operation.MOVE))
            return true;
        else
            return false;
    }
}
