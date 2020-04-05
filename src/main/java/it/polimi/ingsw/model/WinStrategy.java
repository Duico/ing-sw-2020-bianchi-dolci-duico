package it.polimi.ingsw.model;

public interface WinStrategy {
    boolean isWinningMove(Position startPosition, Position destinationPosition, BoardCell[][] grid);

}
