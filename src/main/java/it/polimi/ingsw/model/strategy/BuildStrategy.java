package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;

public interface BuildStrategy extends Serializable {

    /**
     *
     * @param grid Main grid of the game
     * @param startPosition The position where the worker is located at the moment
     * @param destinationPosition The position which the worker wants to build on
     * @param isDome Defines if the worker wants to build a dome
     * @return
     */
    boolean isValidBuild(Position startPosition, Position destinationPosition, Boolean isDome, Board board);

    /**
     *
     * @param numMoves How many times a worker has already moved in the considered Turn
     * @param numBuilds How many times a worker has already built in the considered Turn
     * @param lastOperation Last operation of a worker (can only be 'Build' or 'Move')
     * @return
     */

    default boolean isAllowedToBuild(int numMoves, int numBuilds, Operation lastOperation){
        return (numBuilds == 0 && numMoves >= 1);
    }


    /**
     *
     * @param numMoves How many times a worker has already moved in the considered Turn
     * @param numBuilds How many times a worker has already built in the considered Turn
     * @param lastOperation Last operation of a worker (can only be 'Build' or 'Move')
     * @return
     */
    default boolean isRequiredToBuild(int numMoves, int numBuilds, Operation lastOperation){
        return numBuilds == 0;
    }
}
