package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;

public interface WinStrategy extends Serializable {
    /**
     * check if the movement is a winning move
     * @param startPosition The position where the worker is located at the moment
     * @param destinationPosition The position which the worker wants to move at
     * @param board board of the Game
     * @return true if is a winning move, false in the other case
     */
    boolean isWinningMove(Position startPosition, Position destinationPosition, Board board);

}
