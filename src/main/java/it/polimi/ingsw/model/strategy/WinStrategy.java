package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

public interface WinStrategy {
    /**
     *
     * @param startPosition The position where the worker is located at the moment
     * @param destinationPosition The position which the worker wants to move at
     * @param grid Main grid of the Game
     * @return
     */
    boolean isWinningMove(Position startPosition, Position destinationPosition, BoardCell[][] grid);

}
