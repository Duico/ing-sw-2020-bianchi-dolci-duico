package it.polimi.ingsw.model;

public class DefaultBlock implements BlockStrategy {

    @Override
    public boolean isValidMoveForNextPlayer(Worker worker, BoardCell cell) {
        return false;
    }
}
