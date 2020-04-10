package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

public class DefaultBlock implements BlockStrategy {

    public boolean isBlockMove(Position startPosition, Position destinationPosition, BoardCell[][] grid) {
        return false;
    }
    public boolean blockNextPlayer(Position startPosition, Position destinationPosition, BoardCell[][] grid){
        return false;
    }
}
