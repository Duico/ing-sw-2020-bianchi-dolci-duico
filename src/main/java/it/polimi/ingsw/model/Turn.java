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
        this.currentWorkerId = currentWorkerId;
        this.blockNextPlayer = false;

    }

    public Turn(Player currentPlayer, Card previousTurnCard, boolean previousBlockNextPlayer, Board board){
        this.currentPlayer = currentPlayer;
        this.currentWorkerId = currentWorkerId;
        this.blockNextPlayer = false;
        this.previousTurnCard = previousTurnCard;
        this.previousBlockNextPlayer = previousBlockNextPlayer;
        this.board = board;

    }

    public boolean move(int workerId, Position destinationPosition) throws CloneNotSupportedException, InvalidPushCell, PositionOutOfBoundsException {

        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        Position startPosition = currentPlayer.getCurrentPositionWorker(currentWorkerId);
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(numMoves, numBuilds);
        boolean isOwnWorker = false;
        for(int i=0; i<Player.numWorkers; i++) {
            if(currentPlayer.getCurrentPositionWorker(i).equals(destinationPosition))
                isOwnWorker = true;
        }
        boolean canMove = false;
        try {
            canMove = board.canMove(startPosition, destinationPosition, previousBlockNextPlayer, isOwnWorker, card, previousTurnCard);
        } catch (BlockedMoveException e){
            return false;
        }
        if (isRequiredToMove == false && isAllowedToMove == false) return false;
        else if( canMove == true ){   // to fix, da rivedere

            if(blockNextPlayer == false) {
                blockNextPlayer = board.blockNextPlayer(startPosition, destinationPosition, card);
            }

            Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
            //pushDestPosition can safely be null
            board.putWorkers(startPosition, destinationPosition, pushDestPosition);

            return true;

        }else{
            return false;
        }
    }


    public boolean build (int workerId, Position destinationPosition, boolean isDome) {
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(currentWorkerId);
        Position startPosition = currentPlayer.getCurrentPositionWorker(currentWorkerId);
        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
        boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(numMoves, numBuilds, lastOperation);
        boolean canBuild = board.canBuild(startPosition, destinationPosition, card, isDome);

        if (isRequiredToBuild == false && isAllowedToBuild == false) {
            return false;
        }
        else if( isAllowedToBuild == true && canBuild == true ){  //to fix, da rivedere
            board.build(startPosition, destinationPosition, isDome);
            return true;
        }else
            return false;

    }

    public boolean winMove(int workerId, Position destinationPosition){
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getCurrentPositionWorker(workerId);
        return board.isWinningMove(startPosition, destinationPosition, card);
    }

    public boolean loseCondition(){
        ArrayList<Position> currentPositions = new ArrayList<Position>();
        for (int i=0 ; i<Player.numWorkers; i++) {
            Position positionWorker = currentPlayer.getCurrentPositionWorker(i);
            currentPositions.add(positionWorker);
        }
        boolean isLoseCondition = board.isLoseCondition(currentPositions);
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


}
