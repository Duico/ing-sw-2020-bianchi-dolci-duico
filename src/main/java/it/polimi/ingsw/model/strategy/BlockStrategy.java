package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

import java.io.Serializable;
import java.util.ArrayList;


public interface BlockStrategy extends Serializable {

    boolean isBlockMove(Position startPosition, Position destinationPosition, Board board);
    boolean blockNextPlayer(Position startPosition, Position destinationPosition, Board board);


}
