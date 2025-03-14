package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;

public interface BuildStrategy extends Serializable {

    /**
     *
     * @param startPosition The position where the worker is located at the moment
     * @param destinationPosition The position which the worker wants to build on
     * @param isDome Defines if the worker wants to build a dome
     * @param board board of the game
     * @return true if is a valid building, false in the other case
     */
    boolean isValidBuild(Position startPosition, Position destinationPosition, Boolean isDome, Board board);

    /**
     *
     * @param numMoves How many times a worker has already moved in the considered Turn
     * @param numBuilds How many times a worker has already built in the considered Turn
     * @param lastOperation Last operation of a worker (can only be 'Build' or 'Move')
     * @return true if is allowed to build, false in the other case
     */

    default boolean isAllowedToBuild(int numMoves, int numBuilds, Operation lastOperation){
        return (numBuilds == 0 && numMoves >= 1);
    }


    /**
     *
     * @param numMoves How many times a worker has already moved in the considered Turn
     * @param numBuilds How many times a worker has already built in the considered Turn
     * @param lastOperation Last operation of a worker (can only be 'Build' or 'Move')
     * @return true if is required to build
     */
    default boolean isRequiredToBuild(int numMoves, int numBuilds, Operation lastOperation){
        return numBuilds == 0;
    }
}
