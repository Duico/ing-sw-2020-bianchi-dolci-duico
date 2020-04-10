package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

import java.util.ArrayList;


public interface BlockStrategy {

    boolean isBlockMove(Position startPosition, Position destinationPosition, BoardCell[][] grid);
    boolean blockNextPlayer(Position startPosition, Position destinationPosition, BoardCell[][] grid);


}
