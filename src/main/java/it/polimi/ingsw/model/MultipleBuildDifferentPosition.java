package it.polimi.ingsw.model;

public class MultipleBuildDifferentPosition implements BuildStrategy {

    @Override
    public boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome) {
        BoardCell startCell = grid[startPosition.getX()][startPosition.getY()];
        int numBuilds = startCell.getWorker().getNumBuilds();
        BuildStrategy buildStrategy = new DefaultBuild();
        if(numBuilds == 0){
            return buildStrategy.isValidBuild(grid, startPosition, destinationPosition, isDome);
        }
        else if(numBuilds == 1){
            if(!destinationPosition.equals(startCell.getWorker().getFirstBuild()))
                return buildStrategy.isValidBuild(grid, startPosition, destinationPosition, isDome);
            else
                return false;
        }
            else
                return false;
    }

    @Override
    public boolean isAllowedToBuild(int numMoves, int numBuilds, Operation lastOperation) {
       BuildStrategy MultipleBuildDifferentPosition = new MultipleBuild();
       return MultipleBuildDifferentPosition.isAllowedToBuild(numMoves, numBuilds, lastOperation);
    }

    @Override
    public boolean isRequiredToBuild(int numMoves, int numBuilds, Operation lastOperation) {
        BuildStrategy MultipleBuildDifferentPosition = new MultipleBuild();
        return MultipleBuildDifferentPosition.isRequiredToBuild(numMoves, numBuilds, lastOperation);
    }
}
