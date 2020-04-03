package it.polimi.ingsw.model;

public interface MoveStrategy { //implement DefaultMove functions here

    boolean isValidMove(Worker worker, BoardCell cell);


    default boolean isAllowedToMove(int numMoves){
        if(numMoves != 0){
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
