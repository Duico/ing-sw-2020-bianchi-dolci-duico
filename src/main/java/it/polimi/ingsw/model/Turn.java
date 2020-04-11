package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.BlockedMoveException;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.strategy.MoveStrategy;
import it.polimi.ingsw.model.strategy.OpponentStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Turn implements Serializable {
    private final Player currentPlayer;
    private int currentWorkerId;
    private Card previousTurnCard;
    private List<Board> undoBoards;
    private Board board;
    private boolean blockNextPlayer;
    private boolean previousBlockNextPlayer;

//    public Turn(Player currentPlayer){
//        this.currentPlayer = currentPlayer;
//        //this.currentWorkerId = currentWorkerId;
//        this.currentWorkerId = -1;
//        this.blockNextPlayer = false;
//
//    }


    public Turn(Player currentPlayer, Card previousTurnCard, boolean previousBlockNextPlayer){
        this.currentPlayer = currentPlayer;
        this.currentWorkerId = -1;
        this.blockNextPlayer = false;
        this.previousTurnCard = previousTurnCard;
        this.previousBlockNextPlayer = previousBlockNextPlayer;
    }

    public boolean isAllowedToMove(){
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(numMoves, numBuilds);
        return isAllowedToMove;
    }

    public boolean isRequiredToMove(){
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        return isRequiredToMove;
    }
    public boolean isAnyWorkerNotPlaced(){
        return currentPlayer.isAnyWorkerNotPlaced();
    }

//    public boolean move(Position destinationPosition){
//        // ^maybe throw an error
//        Card card = currentPlayer.getCard();
//        Position startPosition = currentPlayer.getWorkerCurrentPosition(currentWorkerId);
//        boolean isOwnWorker = false;
//        for(int i=0; i<currentPlayer.getNumWorkers(); i++) {
//            if(currentPlayer.getWorkerCurrentPosition(i).equals(destinationPosition))
//                isOwnWorker = true;
//        }
//        boolean canMove = false;
//        try {
//            canMove = board.canMove(startPosition, destinationPosition, previousBlockNextPlayer, isOwnWorker, card, previousTurnCard);
//        } catch (BlockedMoveException e){
//            return false;
//        }
//        if( canMove == true ){
//
//            if(blockNextPlayer == false) {
//                blockNextPlayer = board.blockNextPlayer(startPosition, destinationPosition, card);
//            }
//
//            try {
//                Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
//                board.putWorkers(startPosition, destinationPosition, pushDestPosition);
//            }catch (PositionOutOfBoundsException e){
//                return false;
//            }catch (InvalidPushCell e){
//                return false;
//            }
//            //pushDestPosition can safely be null
//
//            return true;
//
//        }else{
//            return false;
//        }
//    }
    public boolean place(Position placePosition){
        Worker newWorker = new Worker();
        board.setWorker(newWorker, placePosition);
        return currentPlayer.addWorker(newWorker);
    }

    public void move(int workerId, Position destinationPosition) throws PositionOutOfBoundsException, InvalidPushCell{
        Card card = currentPlayer.getCard();
       Position startPosition = currentPlayer.getWorkerCurrentPosition(currentWorkerId);

        if(blockNextPlayer == false) {
            blockNextPlayer(workerId, destinationPosition);
        }
        Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
        board.putWorkers(startPosition, destinationPosition, pushDestPosition);

    }

    public boolean canMove(int workerId, Position destinationPosition) throws BlockedMoveException{
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);

        boolean isOwnWorker = false;
        for(int i=0; i<currentPlayer.getNumWorkers(); i++) {
            if(currentPlayer.getWorkerCurrentPosition(i).equals(destinationPosition))
                isOwnWorker = true;
        }

        MoveStrategy moveStrategy = card.getMoveStrategy();
        OpponentStrategy opponentStrategy = card.getOpponentStrategy();
        boolean isValidMove = moveStrategy.isValidMove(startPosition, destinationPosition, this.board);
        boolean isValidPush = opponentStrategy.isValidPush(startPosition, destinationPosition, isOwnWorker, this.board);

        if( isValidMove == true && isValidPush==true ) {
            return true;
        }else{
            return false;
        }
    }
    public boolean isBlockedMove(int workerId, Position destinationPosition){
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);
        Card card = currentPlayer.getCard();
        return card.getBlockStrategy().isBlockMove( startPosition, destinationPosition, board);
    }
    public boolean getPreviousBlockNextPlayer(){
        return previousBlockNextPlayer;
    }
    public boolean isAllowedToBuild(){
        if(!isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(currentWorkerId);
        boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(numMoves, numBuilds, lastOperation);
        return isAllowedToBuild;
    }

    public boolean blockNextPlayer(int workerId, Position destinationPosition) {
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);
        Card card = currentPlayer.getCard();

        return card.getBlockStrategy().blockNextPlayer(startPosition, destinationPosition, board);
    }

    public boolean isRequiredToBuild(){
        if(!isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(currentWorkerId);
        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
        return isRequiredToBuild;
    }

//
//    public boolean build (Position destinationPosition, boolean isDome) {
//        if(!isWorkersPositionSet())
//            return false;
//        Card card = currentPlayer.getCard();
//        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
//        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
//        Operation lastOperation = currentPlayer.getLastOperationWorker(currentWorkerId);
//        Position startPosition = currentPlayer.getWorkerCurrentPosition(currentWorkerId);
//        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
//        boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(numMoves, numBuilds, lastOperation);
//        boolean canBuild = board.canBuild(startPosition, destinationPosition, card, isDome);
//
//        if( canBuild == true ){
//            board.build(startPosition, destinationPosition, isDome);
//            return true;
//        }else
//            return falisWorkersPositionSetse;
//
//    }

    public boolean winMove(Position destinationPosition){
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(currentWorkerId);
        return board.isWinningMove(startPosition, destinationPosition, card);
    }

    public boolean checkLoseCondition() throws PositionOutOfBoundsException {
        Card card = currentPlayer.getCard();
        ArrayList<Position> currentPositions = new ArrayList<Position>();
        for (int i=0 ; i<currentPlayer.getNumWorkers(); i++) {
            Position positionWorker = currentPlayer.getWorkerCurrentPosition(i);
            currentPositions.add(positionWorker);
        }
        boolean isLoseCondition = board.isLoseCondition(currentPositions, previousBlockNextPlayer, card, previousTurnCard);
        return isLoseCondition;
    }

    public boolean checkPlayer(Player viewPlayer){
        return currentPlayer.getUuid() == viewPlayer.getUuid();
    }

    // private Player getCurrentPlayer() {
    //        return currentPlayer;
    //    }

    public int getCurrentWorkerId(){
        return this.currentWorkerId;
    }

    public boolean isBlockNextPlayer() {
        return blockNextPlayer;
    }

    public boolean updateCurrentWorker(int currentWorkerId){
        if(this.currentWorkerId<0){
            setCurrentWorker(currentWorkerId);
            return true;
        }else
            return false;
    }

    private boolean setCurrentWorker(int currentWorkerId){
        currentPlayer.isWorkerSet()
        this.currentWorkerId=currentWorkerId;
    }

    private void saveBoard(){
        undoBoards.add(board);
        board = board.clone();
    }
}
