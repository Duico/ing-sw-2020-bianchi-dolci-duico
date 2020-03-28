package it.polimi.ingsw.model;

public class DefaultWin implements WinStrategy {

    @Override
    public boolean isWinningMove(Worker worker, BoardCell cell) {
        return false;
    }

    @Override
    public boolean isWinningBuild(Worker worker, BoardCell cell) {
        return false;
    }
}
