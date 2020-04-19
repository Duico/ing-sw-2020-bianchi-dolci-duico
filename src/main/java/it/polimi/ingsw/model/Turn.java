package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class Turn implements Serializable {
    protected final Player currentPlayer;
    protected boolean isUndoAvailable = false; // false when deserializing for undo
    final private TurnPhase phase;


    public Turn(TurnPhase turnPhase, Player currentPlayer){
        this.phase = turnPhase;
        this.currentPlayer = currentPlayer;

    }
    public TurnPhase getPhase(){
        return this.phase;
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
    private boolean canBuild(Board board, int workerId){
        return false;
    }
    private boolean canMove(Board board, int workerId){
        return false;
    }

    public boolean isAnyWorkerNotPlaced(){
        return currentPlayer.isAnyWorkerNotPlaced();
    }

    public boolean isFeasibleMove(Board board, int workerId, Position destinationPosition){
        return false;
    }
    public boolean isFeasibleBuild(Board board, int workerId, Position destinationPosition, boolean isDome){
        return false;
    }
    public boolean isBlockedMove(Board board, int workerId, Position destinationPosition) {
        return false;
    }
    public boolean getPreviousBlockNextPlayer(){
        return false;
    }
    public boolean getBlockNextPlayer(){
        return false;
    }
    public boolean getPrevicanBuildousBlockNextPlayer(){
        return false;
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
    public boolean isWinningMove(Board board, Position destinationPosition){
        return false;
    }

    public boolean checkCurrentWorker(int workerId){
        return false;
    }

    boolean isLoseCondition(Board board) {
        return false;
    }

    boolean cannotMakeRequiredOperation(Board board, int workerId){
        boolean isRequiredToMove = this.isRequiredToMove(workerId);
        boolean isRequiredToBuild = this.isRequiredToBuild(workerId);

        if(isRequiredToBuild && isRequiredToMove){
            if(!canBuild(board, workerId) && !canMove(board, workerId))
                return true;
        }else if(isRequiredToBuild){
            if(!canBuild(board, workerId))
                return true;
        }else if(isRequiredToMove){//should be impossible
            if(!canMove(board, workerId))
                return true;
        }
        return false;
    }

}
