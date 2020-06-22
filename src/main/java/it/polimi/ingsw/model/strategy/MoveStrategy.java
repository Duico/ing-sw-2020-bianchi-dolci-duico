package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;

public interface MoveStrategy extends Serializable {

    /**
     *
     * @param startPosition position where a worker is currently located
     * @param destPosition position where a worker is moving to
     * @param board main Board of the game
     * @return true if a movement from start to destination position can be done
     */
    boolean isValidMove(Position startPosition, Position destPosition, Board board);

    /**
     * defines if a worker is allowed to move
     * @param numMoves number of moves already done by current worker
     * @param numBuilds number of buildings already done by current worker
     * @return true if worker is allowed to move
     */
    default boolean isAllowedToMove(int numMoves, int numBuilds){
        if(numMoves != 0 /*&& numBuilds==0*/){
            return false;
        }else{
            return true;
        }
    }

    /**
     * defines if a worker is allowed to build
     * @param numMoves number of moves already done by current worker
     * @return true if worker is allowed to build
     */
    default boolean isRequiredToMove(int numMoves){
        if(numMoves != 0){
            return false;
        }else{
            return true;
        }

    }

}
