package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class Turn implements Serializable {
    final Player currentPlayer;
    protected boolean isUndoAvailable = false; // false when deserializing for undo
    final private TurnPhase phase;


    public Turn(TurnPhase turnPhase, Player currentPlayer){
        this.phase = turnPhase;
        this.currentPlayer = currentPlayer;

    }
    public boolean isAllowedToMove(){
        return false;
    }
    public boolean isAllowedToMove(int workerId){
        return false;
    }
    public boolean isRequiredToMove(){
        return false;
    }
    public boolean isRequiredToMove(int workerId){
        return false;
    }
    public boolean isRequiredToBuild(){
        return false;
    }
    public boolean isRequiredToBuild(int workerId){
        return false;
    }
    public boolean isAllowedToBuild(){
        return false;
    }
    public boolean isAllowedToBuild(int workerId){
        return false;
    }
    public boolean isAnyWorkerNotPlaced(){
        return currentPlayer.isAnyWorkerNotPlaced();
    }
    public boolean getBlockNextPlayer(){
        return false;
    }
    public boolean getPreviousBlockNextPlayer(){
        return false;
    }
    public boolean checkPlayer(Player viewPlayer){
        return currentPlayer.getUuid() == viewPlayer.getUuid();
    }
//     private Player getCurrentPlayer() {
////            return currentPlayer;
////        }
    public UUID getCurrentPlayerUUID() {
        return currentPlayer.getUuid();
    }
    protected Player getCurrentPlayer() {
        return currentPlayer;
    }
    public boolean isSetCurrentWorker(){
        return false;
    }
    public boolean isBlockNextPlayer() {
        return false;
    }

    public TurnPhase getPhase(){
        return this.getPhase();
    }

}
