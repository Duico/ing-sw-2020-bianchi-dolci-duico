package it.polimi.ingsw.model;

import java.util.ArrayList;

public class DefaultBlock implements BlockStrategy {

    public boolean isBlockMove(Position startPosition, Position destinationPosition, BoardCell[][] grid) {
        return false;
    }
    public boolean blockNextPlayer(Position startPosition, Position destinationPosition, BoardCell[][] grid){
        return false;
    }
}
