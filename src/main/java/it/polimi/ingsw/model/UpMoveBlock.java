package it.polimi.ingsw.model;

import java.util.ArrayList;

public class UpMoveBlock implements BlockStrategy {
    @Override
    public boolean isBlockMove(Position startPosition, Position destinationPosition, BoardCell[][] grid) {
        BoardCell startCell = grid[startPosition.getX()][startPosition.getY()];
        BoardCell destCell = grid[destinationPosition.getY()][destinationPosition.getY()];
        if (startCell.getLevel().ordinal()<destCell.getLevel().ordinal())
            return true;
        else
            return false;

    }

    public boolean blockNextPlayer(Position startPosition, Position destinationPosition, BoardCell[][] grid){
        BoardCell startCell = grid[startPosition.getX()][startPosition.getY()];
        BoardCell destCell = grid[destinationPosition.getY()][destinationPosition.getY()];
        if (startCell.getLevel().ordinal()<destCell.getLevel().ordinal())
            return true;
        else
            return false;
    }
}
