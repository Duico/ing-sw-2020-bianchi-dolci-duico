package it.polimi.ingsw.model;

import java.util.ArrayList;

public interface BuildStrategy {
    boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome, ArrayList<Position> builds);

    default boolean isAllowedToBuildDome(BoardCell [][] grid, Position buildPosition)
    {
        BoardCell buildCell = grid[buildPosition.getX()][buildPosition.getY()];
        return (buildCell.getLevel().ordinal() == 3 && buildCell.getWorker() == null && !buildCell.hasDome());
    }

    default boolean isAllowedToBuild(ArrayList<Operation> operations){
        if(operations.size() == 1 && operations.get(0) == Operation.MOVE)
            return true;
        else
            return false;
    }


    default boolean isRequiredToBuild(ArrayList<Operation> operations){
        if(operations.size() <= 1)
            return true;
        else
            return false;
    }
}
