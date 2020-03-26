package it.polimi.ingsw.model;

public class DefaultWin implements WinStrategy {
    @Override
    public boolean isWinningMove(BoardCell cell) {
        return false;
    }

    @Override
    public boolean isWinningBuild(BoardCell cell) {
        return false;
    }
}
