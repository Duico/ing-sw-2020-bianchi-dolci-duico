package it.polimi.ingsw.model;

import java.util.ArrayList;

public class MultipleBuildDifferentPosition implements BuildStrategy {

    @Override
    public boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome, ArrayList<Position> builds) {
        int numBuilds = grid[startPosition.getX()][startPosition.getY()].getWorker().getNumBuilds();
        BuildStrategy buildStrategy = new DefaultBuild();
        if(numBuilds == 0){
            return buildStrategy.isValidBuild(grid, startPosition, destinationPosition, isDome, builds);
        }
        else if(numBuilds==1){
            if(destinationPosition != builds.get(0))
                return buildStrategy.isValidBuild(grid, startPosition, destinationPosition, isDome, builds);
            else
                return false;
        }
            else
                return false;
    }

    @Override
    public boolean isAllowedToBuild(ArrayList<Operation> operations) {
       BuildStrategy MultipleBuildDifferentPosition = new MultipleBuild();
       return MultipleBuildDifferentPosition.isAllowedToBuild(operations);
    }

    @Override
    public boolean isRequiredToBuild(ArrayList<Operation> operations) {
        BuildStrategy MultipleBuildDifferentPosition = new MultipleBuild();
        return MultipleBuildDifferentPosition.isRequiredToBuild(operations);
    }
}
