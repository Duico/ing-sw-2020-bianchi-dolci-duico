package it.polimi.ingsw.model;

public interface WinStrategy {
    boolean isWinningMove(Worker worker, BoardCell cell);
    boolean isWinningBuild(Worker worker, BoardCell cell);
}
