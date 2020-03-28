package it.polimi.ingsw.model;

public class UpMoveBlock implements BlockStrategy {
    @Override
    public boolean isValidMoveForNextPlayer(Worker worker, BoardCell cell) {
        return false;
    }
}
