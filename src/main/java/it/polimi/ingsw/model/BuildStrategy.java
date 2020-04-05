package it.polimi.ingsw.model;

public interface BuildStrategy {

    boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome);


    default boolean isAllowedToBuild(int numMoves, int numBuilds, Operation lastOperation){
        return (numBuilds == 0 && numMoves >= 1);
    }


    default boolean isRequiredToBuild(int numMoves, int numBuilds, Operation lastOperation){
        return numBuilds == 0;
    }
}
