package it.polimi.ingsw.model;

public class UpMoveBlock implements BlockStrategy {
    public boolean isValidMoveForNextPlayer(BoardCell cell) {
        return false;
    }
}
