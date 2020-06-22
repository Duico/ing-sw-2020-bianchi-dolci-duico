package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;


public interface BlockStrategy extends Serializable {

    /**
     *
     * @param startPosition position where a worker is currently located
     * @param destinationPosition position where a worker is moving to
     * @param board main Board of the game
     * @return true if a movement from start to destination position can't be done
     */
    boolean isBlockMove(Position startPosition, Position destinationPosition, Board board);

    /**
     *
     * @param startPosition position where a worker is currently located
     * @param destinationPosition position where a worker is moving to
     * @param board main Board of the game
     * @return true if a movement from start to destination position is going to block next player's movement
     */
    boolean blockNextPlayer(Position startPosition, Position destinationPosition, Board board);


}
