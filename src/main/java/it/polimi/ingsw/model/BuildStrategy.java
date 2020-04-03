package it.polimi.ingsw.model;

import java.util.ArrayList;

public interface BuildStrategy {
    boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome, ArrayList<Position> builds);

    default boolean isAllowedToBuildDome(BoardCell [][] grid, Position buildPosition)
    {
        BoardCell buildCell = grid[buildPosition.getX()][buildPosition.getY()];
        return (buildCell.getLevel().ordinal() == 2 && buildCell.getWorker() == null && !buildCell.hasDome());
    }

    default boolean isAllowedToBuild(int numBuilds, ArrayList<Operation> operations){
        return numBuilds == 0;
    }


    default boolean isRequiredToBuild(int numBuilds, ArrayList<Operation> operations){
        return numBuilds == 0;

    }
}
