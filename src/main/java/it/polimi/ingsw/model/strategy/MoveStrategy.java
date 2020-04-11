package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Board;

public interface MoveStrategy { //implement DefaultMove functions here

    boolean isValidMove(Position startPosition, Position destPosition, Board board);

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
