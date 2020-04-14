package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;

public interface WinStrategy extends Serializable {
    /**
     *
     * @param startPosition The position where the worker is located at the moment
     * @param destinationPosition The position which the worker wants to move at
     * @param grid Main grid of the Game
     * @return
     */
    boolean isWinningMove(Position startPosition, Position destinationPosition, Board board);

}
