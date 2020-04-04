package it.polimi.ingsw.model;

import java.util.ArrayList;


public interface BlockStrategy {

    boolean isBlockMove(Position startPosition, Position destinationPosition, BoardCell[][] grid);
    boolean blockNextPlayer(Position startPosition, Position destinationPosition, BoardCell[][] grid);


}
