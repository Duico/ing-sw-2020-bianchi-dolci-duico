package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;

public class DefaultBlock implements BlockStrategy {

    public boolean isBlockMove(Position startPosition, Position destinationPosition, Board board) {
        return false;
    }
    public boolean blockNextPlayer(Position startPosition, Position destinationPosition, Board board){
        return false;
    }
}
