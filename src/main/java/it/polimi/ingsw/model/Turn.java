package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.UUID;

public class Turn implements Serializable {
    private final Player currentPlayer;
    private int currentWorkerId;
    private Card previousTurnCard;
    protected boolean isUndoAvailable = false; // false when deserializing for undo
    private boolean blockNextPlayer;
    private boolean previousBlockNextPlayer;

    public Turn(Player currentPlayer, Card previousTurnCard, boolean previousBlockNextPlayer){
        this.currentPlayer = currentPlayer;
        this.currentWorkerId = -1;
        this.blockNextPlayer = false;
        this.previousTurnCard = previousTurnCard;
        this.previousBlockNextPlayer = previousBlockNextPlayer;
    }
    public boolean isAllowedToMove(){
        return isAllowedToMove(currentWorkerId);
    }
    public boolean isAllowedToMove(int workerId){
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(numMoves, numBuilds);
        return isAllowedToMove;
    }
    public boolean isRequiredToMove(){
        return isRequiredToMove(currentWorkerId);
    }
    public boolean isRequiredToMove(int workerId){
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        return isRequiredToMove;
    }

    public boolean isRequiredToBuild(){
        return isRequiredToBuild(currentWorkerId);
    }
    public boolean isRequiredToBuild(int workerId){
        if(!isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(workerId);
        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
        return isRequiredToBuild;
    }

    public boolean isAllowedToBuild(){
        return isAllowedToBuild(currentWorkerId);
    }
    public boolean isAllowedToBuild(int workerId){
        if(!isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(workerId);
        boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(numMoves, numBuilds, lastOperation);
        return isAllowedToBuild;
    }

    public boolean isAnyWorkerNotPlaced(){
        return currentPlayer.isAnyWorkerNotPlaced();
    }

    public boolean getBlockNextPlayer(){
        return blockNextPlayer;
    }
    public boolean getPreviousBlockNextPlayer(){
        return previousBlockNextPlayer;
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

    public int getCurrentWorkerId(){
        return this.currentWorkerId;
    }

    public boolean isSetCurrentWorker(){
        return currentWorkerId>=0;
    }

    public boolean isBlockNextPlayer() {
        return blockNextPlayer;
    }


    public boolean updateCurrentWorker(int currentWorkerId){
        if(!isSetCurrentWorker()){
            setCurrentWorker(currentWorkerId);
            return true;
        }else
            return false;
    }

    private void setCurrentWorker(int currentWorkerId){
        if(currentPlayer.isWorkerSet(currentWorkerId))
            this.currentWorkerId=currentWorkerId;
    }


    public Card getPreviousTurnCard(){
        return this.previousTurnCard;
    }

    protected Player getCurrentPlayer() {
        return currentPlayer;
    }
}
