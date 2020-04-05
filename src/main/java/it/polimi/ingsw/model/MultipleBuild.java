package it.polimi.ingsw.model;

public class MultipleBuild implements BuildStrategy {

    @Override
    public boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome) {
        try{
            BoardCell startCell = grid[startPosition.getX()][startPosition.getY()];
            int numBuilds = startCell.getWorker().getNumBuilds();
            BuildStrategy buildStrategy = new DefaultBuild();
            if(numBuilds == 0){
                return buildStrategy.isValidBuild(grid, startPosition, destinationPosition, isDome);
            }
            else if(numBuilds == 1){
                if(destinationPosition.equals(startCell.getWorker().getFirstBuild()))
                    return (buildStrategy.isValidBuild(grid, startPosition, destinationPosition, isDome) && !isDome);
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
