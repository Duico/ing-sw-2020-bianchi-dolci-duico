package it.polimi.ingsw.model;


import java.util.ArrayList;

public class BuildAtFirstBuild implements BuildStrategy {

    @Override
    public boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome, ArrayList<Position> builds)
    {
        BuildStrategy buildAtFirstBuild = new DefaultBuild();
        return buildAtFirstBuild.isValidBuild(grid, startPosition, destinationPosition, isDome, builds);
    }


    @Override
    public boolean isAllowedToBuild(ArrayList<Operation> operations) {
        if(operations.size() == 0)
            return true;
        else if(operations.size() == 2 && operations.get(0) == Operation.BUILD)
            return true;
        else
            return false;
    }

    @Override
    public boolean isRequiredToBuild(ArrayList<Operation> operations) {
        if(operations.size() <= 1)
            return true;
        else if(operations.size() == 2 && operations.get(0) == Operation.BUILD)
            return true;
        else
            return false;
    }
}
