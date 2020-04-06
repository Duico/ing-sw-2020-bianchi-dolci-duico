package it.polimi.ingsw.model;

public interface MoveStrategy { //implement DefaultMove functions here

    boolean isValidMove(Position startPosition, Position destPosition, BoardCell[][] grid);

    default boolean isAllowedToMove(int numMoves, int numBuilds){
        if(numMoves != 0 && numBuilds==0){
            return false;
        }else{
            return true;
        }
    }

    default boolean isRequiredToMove(int numMoves){
        if(numMoves != 0){
            return false;
        }else{
            return true;
        }

    }

}
