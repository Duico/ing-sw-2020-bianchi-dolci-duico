package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Turn {
    private final Player currentPlayer;
    private int currentWorkerId;
    private Worker currentWorker; //clone
    private Turn previousTurn;
    private Board board;

    public Turn(Player currentPlayer){
        this.currentPlayer = currentPlayer;
        this.currentWorkerId = currentWorkerId;
    }

    public boolean move (int workerId, Position destinationPosition) throws CloneNotSupportedException, InvalidPushCell, PositionOutOfBoundsException {

        Worker currentWorker = currentPlayer.getWorker(workerId);
        try {
            this.setCurrentWorker(currentWorker);
        } catch (WorkerAlreadySetException e) {
            //If he already moved or built STOP
            //else ask to switch worker
        }
        //check worker position

        Card card = currentPlayer.getCard();
        Player previousPlayer = previousTurn.getCurrentPlayer();
        Card previousCard = previousPlayer.getCard();
        ArrayList<Position> previousMoves = previousTurn.getCurrentWorker().getMoves(); //TODO at the end of the turn Turn.currentWorker must be updated
        Position startPosition = currentWorker.getCurrentPosition();
        int numMoves = currentWorker.getNumMoves();   // spostare nel player metodo che restiuisce numMoves
        int numBuilds = currentWorker.getNumBuilds();
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(numMoves);
        boolean canMove = false;
        try {
            canMove = board.canMove(startPosition, destinationPosition, previousMoves, card, previousCard);
        } catch (BlockedMoveException e){
            return false;
        }
        if (isRequiredToMove == false && isAllowedToMove == false) return false;
        else if( canMove == true ){
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

    public int getCurrentWorkerId(){
        return this.currentWorkerId;
    }

    private void setCurrentWorker(Worker currentWorker) throws WorkerAlreadySetException {
        if(this.currentWorker == null){
            this.currentWorker = currentWorker;
        }else{
            throw new WorkerAlreadySetException();
        }
    }
    private Worker getCurrentWorker() {

    }

}
