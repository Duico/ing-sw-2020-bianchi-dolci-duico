package it.polimi.ingsw.model;

public interface WinStrategy {
    boolean isWinningMove(BoardCell cell);
    boolean isWinningBuild(BoardCell cell);
}
