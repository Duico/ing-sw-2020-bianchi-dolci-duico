package it.polimi.ingsw.model;

public class DefaultBlock implements BlockStrategy {
    @Override
    public boolean isValidMoveForNextPlayer(BoardCell cell) {
        return false;
    }
}
