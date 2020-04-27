package it.polimi.ingsw.model;

import java.io.PipedOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class Turn implements Serializable {
    final Player currentPlayer;
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
    public boolean isAllowedToMove(Position workerPosition){
        return false;
    }
    public boolean isRequiredToMove(){
        return false;
    }
    public boolean isRequiredToMove(Position workerPosition){
        return false;
    }
    public boolean isRequiredToBuild(){
        return false;
    }
    public boolean isRequiredToBuild(Position workerPosition){
        return false;
    }
    public boolean isAllowedToBuild(){
        return false;
    }
    public boolean isAllowedToBuild(Position workerPosition){
        return false;
    }
    protected boolean canBuild(Board board, Position workerPosition){
        return false;
    }
    protected boolean canMove(Board board, Position workerPosition){
        return false;
    }

    public boolean isAnyWorkerNotPlaced(){
        return currentPlayer.isAnyWorkerNotPlaced();
    }

    public boolean isFeasibleMove(Board board, Position startPosition, Position destinationPosition){
        return false;
    }
    public boolean isFeasibleBuild(Board board, Position startPosition, Position destinationPosition, boolean isDome){
        return false;
    }
    public boolean isBlockedMove(Board board, Position startPosition, Position destinationPosition) {
        return false;
    }
    public boolean getPreviousBlockNextPlayer(){
        return false;
    }
    public boolean getBlockNextPlayer(){
        return false;
    }

//     private Player getCurrentPlayer() {
////            return currentPlayer;
////        }
//    public UUID getCurrentPlayerUUID() {
//        return currentPlayer.getUuid();
//    }
    public boolean isCurrentPlayerUUID(UUID playerId){
        return currentPlayer.getUuid() == playerId;
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
//    public boolean isWinningMove(Board board, Position destinationPosition){
//        return false;
//    }

    public boolean checkCurrentWorker(Position workerPosition){
        return false;
    }

    boolean isLoseCondition(Board board) {
        return false;
    }

    boolean cannotMakeRequiredOperation(Board board, Position workerPosition){
        boolean isRequiredToMove = isRequiredToMove(workerPosition);
        boolean isRequiredToBuild = isRequiredToBuild(workerPosition);

        if(isRequiredToBuild && isRequiredToMove){
            if(!canBuild(board, workerPosition) && !canMove(board, workerPosition))
                return true;
        }else if(isRequiredToBuild){
            if(!canBuild(board, workerPosition))
                return true;
        }else if(isRequiredToMove){//should be impossible
            if(!canMove(board, workerPosition))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turn turn = (Turn) o;
        return /*isUndoAvailable == turn.isUndoAvailable &&*/
                Objects.equals(getCurrentPlayer(), turn.getCurrentPlayer()) &&
                getPhase() == turn.getPhase();
    }

}
