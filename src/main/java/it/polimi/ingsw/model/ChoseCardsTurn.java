package it.polimi.ingsw.model;

public class ChoseCardsTurn extends Turn {

    public ChoseCardsTurn(Player currentPlayer) {
        super(TurnPhase.CHOSE_CARDS, currentPlayer);
    }

    public boolean canBuild(Board board, int workerId){
        return false;
    }
    public boolean canMove(Board board, int workerId){
        return false;
    }

    public boolean isRequiredToBuild(int workerId){
        return false;
    }

    public boolean isRequiredToMove(int workerId){
        return false;
    }
}
