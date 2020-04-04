package it.polimi.ingsw.model;

import java.util.ArrayList;

public class MultipleBuild implements BuildStrategy {

    @Override
    public boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome, ArrayList<Position> builds) {
        int numBuilds = grid[startPosition.getX()][startPosition.getY()].getWorker().getNumBuilds();
        BuildStrategy buildStrategy = new DefaultBuild();
        if(numBuilds==0){
            return buildStrategy.isValidBuild(grid, startPosition, destinationPosition, isDome, builds);
        }
        else if(numBuilds==1){
            if(destinationPosition == builds.get(0))
                return (buildStrategy.isValidBuild(grid, startPosition, destinationPosition, isDome, builds) && !isDome);
            else
                return false;
        }
        else
            return false;
    }

    @Override
    public boolean isAllowedToBuild(ArrayList<Operation> operations) {
        if(operations.size() == 0)
            return false;
        else if(operations.size() == 1 && operations.get(0) == Operation.MOVE)
            return true;
        else if(operations.size() == 2 && operations.get(1) == Operation.BUILD && operations.get(0) == Operation.MOVE)
            return true;
        else return false;
    }

    @Override
    public boolean isRequiredToBuild(ArrayList<Operation> operations) {
        if(operations.size() == 0)
            return true;
        else if(operations.size() == 1 && operations.get(0) == Operation.MOVE)
            return true;
        else
            return false;
    }
}
