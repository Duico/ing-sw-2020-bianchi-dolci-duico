package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.BlockedMoveException;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;

import java.io.Serializable;
import java.util.ArrayList;

public class Turn implements Serializable {
    private final Player currentPlayer;
    private boolean blockNextPlayer;
    private int currentWorkerId;
    private Card previousTurnCard;
    private boolean previousBlockNextPlayer;
    private Board board;

    public Turn(Player currentPlayer){
        this.currentPlayer = currentPlayer;
        //this.currentWorkerId = currentWorkerId;
        this.currentWorkerId = -1;
        this.blockNextPlayer = false;

    }

    public Turn(Player currentPlayer, Card previousTurnCard, boolean previousBlockNextPlayer, Board board){
        this.currentPlayer = currentPlayer;
        this.currentWorkerId = -1;
        this.blockNextPlayer = false;
        this.previousTurnCard = previousTurnCard;
        this.previousBlockNextPlayer = previousBlockNextPlayer;
        this.board = board;

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

    public boolean move(Position destinationPosition){
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getCurrentPositionWorker(currentWorkerId);
        boolean isOwnWorker = false;
        for(int i=0; i<currentPlayer.getNumWorkers(); i++) {
            if(currentPlayer.getCurrentPositionWorker(i).equals(destinationPosition))
                isOwnWorker = true;
        }
        boolean canMove = false;
        try {
            canMove = board.canMove(startPosition, destinationPosition, previousBlockNextPlayer, isOwnWorker, card, previousTurnCard);
        } catch (BlockedMoveException e){
            return false;
        }
        if( canMove == true ){

            if(blockNextPlayer == false) {
                blockNextPlayer = board.blockNextPlayer(startPosition, destinationPosition, card);
            }

            try {
                Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
                board.putWorkers(startPosition, destinationPosition, pushDestPosition);
            }catch (PositionOutOfBoundsException e){
                return false;
            }catch (InvalidPushCell e){
                return false;
            }
            //pushDestPosition can safely be null

            return true;

        }else{
            return false;
        }
    }

    public boolean isAllowedToBuild(){
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(currentWorkerId);
        boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(numMoves, numBuilds, lastOperation);
        return isAllowedToBuild;
    }

    public boolean isRequiredToBuild(){
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(currentWorkerId);
        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
        return isRequiredToBuild;
    }


    public boolean build (Position destinationPosition, boolean isDome) {
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(currentWorkerId);
        Position startPosition = currentPlayer.getCurrentPositionWorker(currentWorkerId);
        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
        boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(numMoves, numBuilds, lastOperation);
        boolean canBuild = board.canBuild(startPosition, destinationPosition, card, isDome);

        if( canBuild == true ){
            board.build(startPosition, destinationPosition, isDome);
            return true;
        }else
            return false;

    }

    public boolean winMove(Position destinationPosition){
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getCurrentPositionWorker(currentWorkerId);
        return board.isWinningMove(startPosition, destinationPosition, card);
    }

    public boolean checkLoseCondition() throws PositionOutOfBoundsException {
        Card card = currentPlayer.getCard();
        ArrayList<Position> currentPositions = new ArrayList<Position>();
        for (int i=0 ; i<currentPlayer.getNumWorkers(); i++) {
            Position positionWorker = currentPlayer.getCurrentPositionWorker(i);
            currentPositions.add(positionWorker);
        }
        boolean isLoseCondition = board.isLoseCondition(currentPositions, previousBlockNextPlayer, card, previousTurnCard);
        return isLoseCondition;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

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

    private void setCurrentWorker(int currentWorkerId){
        this.currentWorkerId=currentWorkerId;
    }


}
