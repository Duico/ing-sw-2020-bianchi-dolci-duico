package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private final Player currentPlayer;
    private int currentWorker;
    private Turn previousTurn;
    private Board board;

    public Turn(Player currentPlayer){
        this.currentPlayer = currentPlayer;
        this.currentWorker = currentWorker;
    }

    public boolean move (Position destinationPosition) throws CloneNotSupportedException, InvalidPushCell, PositionOutOfBoundsException {

        Card card = currentPlayer.getCard();
        Card previousCard = previousTurn.getCurrentPlayer().getCard();
        Position startPosition = currentPlayer.getWorker(currentWorker).getCurrentPosition();
        int numMoves = currentPlayer.getWorker(currentWorker).getNumMoves();   // spostare nel player metodo che restiuisce numMoves
        int numBuilds = currentPlayer.getWorker(currentWorker).getNumBuilds();
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(numMoves);
        boolean canMove = board.canMove(startPosition, destinationPosition, card);
        boolean isNotBlocked = board.isBlockMove(startPosition, destinationPosition, previousCard);

        if (isRequiredToMove == false && isAllowedToMove == false) return false;
        else if( canMove == true && ){
            Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
            if( pushDestPosition != null){
                board.pushWorkersInTheCells(startPosition, destinationPosition, pushDestPosition);
            }else {
                board.setWorkerInTheCell(startPosition, destinationPosition);
            }

            return true;
        }else{
            return false;
        }
    }

    public boolean build (Position startPosition, Position destinationPosition){
        return false;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentWorker(){
        return this.currentWorker;
    }




}
