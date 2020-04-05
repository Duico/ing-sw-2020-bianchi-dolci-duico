package it.polimi.ingsw.model;

import java.io.Serializable;

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
        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
        Position startPosition = currentPlayer.getCurrentPositionWorker(currentWorkerId);
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(numMoves);
        boolean canMove = false;
        try {
            canMove = board.canMove(startPosition, destinationPosition, previousBlockNextPlayer, card, previousTurnCard);
        } catch (BlockedMoveException e){
            return false;
        }
        if (isRequiredToMove == false && isAllowedToMove == false) return false;
        else if( canMove == true ){

            if(blockNextPlayer == false) {
                blockNextPlayer = board.blockNextPlayer(startPosition, destinationPosition, card);
            }

            Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
            if( pushDestPosition != null){
                board.putWorkers(startPosition, destinationPosition, pushDestPosition);
            }else {
                board.putWorkers(startPosition, destinationPosition, null);
            }

            return true;

        }else{
            return false;
        }
    }


    public boolean build (Position startPosition, Position destinationPosition){
        return false;
    }

    public boolean winMove(int workerId, Position destinationPosition){
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getCurrentPositionWorker(workerId);
        return board.isWinningMove(startPosition, destinationPosition, card);
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
