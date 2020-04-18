package it.polimi.ingsw.model;

import java.util.UUID;

public class NormalTurn extends Turn {
    private int currentWorkerId;
    private Card previousTurnCard;
    private boolean blockNextPlayer;
    private boolean previousBlockNextPlayer;

    public NormalTurn(Player currentPlayer, Card previousTurnCard, boolean previousBlockNextPlayer){
        super(TurnPhase.NORMAL, currentPlayer);
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
        if(isAnyWorkerNotPlaced())
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
        if(isAnyWorkerNotPlaced())
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

    public int getCurrentWorkerId(){
        return this.currentWorkerId;
    }

    public boolean checkCurrentWorker(int workerId){
        return  !isSetCurrentWorker() || (currentWorkerId == workerId );
    }

    public boolean isSetCurrentWorker(){
        return currentWorkerId>=0;
    }

    public boolean isBlockNextPlayer() {
        return blockNextPlayer;
    }


    public boolean updateCurrentWorker(int workerId){
        if(!isSetCurrentWorker()){
            setCurrentWorker(workerId);
            return true;
        }else
            return false;
    }

    private void setCurrentWorker(int workerId){
        if(currentPlayer.isWorkerSet(workerId))
            this.currentWorkerId=workerId;
    }


    public Card getPreviousTurnCard(){
        return this.previousTurnCard;
    }

    protected Player getCurrentPlayer() {
        return currentPlayer;
    }
}
