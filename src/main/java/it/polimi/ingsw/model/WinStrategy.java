package it.polimi.ingsw.model;

public interface WinStrategy {
    boolean isWinningMove(BoardCell[][] grid, Position startPosition, Position destinationPosition);

}
