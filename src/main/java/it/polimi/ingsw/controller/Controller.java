package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.BlockedMoveException;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.view.event.MoveViewEvent;
import it.polimi.ingsw.view.event.PlaceViewEvent;
import it.polimi.ingsw.view.event.ViewEvent;
import it.polimi.ingsw.view.event.ViewEventListener;


public class Controller implements ViewEventListener {

    private Game game; //refer to our model

    public Controller(Game game) {
        this.game = game;
    }


    public void handleEvent(ViewEvent message) {
        System.out.println("Generic event from view");
    }

//    public void handleEvent(MoveViewEvent message) {
//        Player player = game.getCurrentPlayer();
//
//        int worker = message.getWorkerId();
//        if (message.getPlayer() != player) return; //todo an event Wrong player
//
//        Turn turn = game.getTurn();
//        turn.safeMove(worker, message.getDestPosition());
//
//    }

    public void move(MoveViewEvent message){
        Turn turn = game.getTurn();
        int currentWorkerId = message.getWorkerId();
        Player viewPlayer = message.getPlayer();
        //CHECK player
        checkPlayer(message);
        turn.updateCurrentWorker(currentWorkerId);


        Position destinationPosition = message.getDestPosition();

        if(!turn.isAllowedToMove()){
            //STOP
            //notify view
        }

        if(turn.isAnyWorkerNotPlaced()){
            //STOP
            //notify view
        }

        //check block
        boolean canMove = true;

        if(turn.getPreviousBlockNextPlayer()) {
            if(turn.isBlockedMove(currentWorkerId, destinationPosition)){
                canMove = false;

                //notify RemoteView
            }
        }
        //check isValidMove and isValidPush

        try {
            if(!turn.canMove(currentWorkerId, destinationPosition)){
                canMove = false;
                //notify view
            }

        } catch (BlockedMoveException e){
            //notify view
        }


        if( canMove == true ) {
            try {
                turn.move(currentWorkerId, destinationPosition);
            } catch (PositionOutOfBoundsException e) {
                e.printStackTrace();
            } catch (InvalidPushCell invalidPushCell) {
                invalidPushCell.printStackTrace();
            }
            //notify view of success/failure
        }
    }
//TODO now

//    public void place(PlaceViewEvent message){
//        Turn turn = game.getTurn();
//        checkPlayer(message);
//
//        if(!turn.isAnyWorkerNotPlaced()){
//
//            //all workers are placed
//            //notify view
//        }
//            turn.place(message.getDestPosition());
//        }
//    }

    //TODO now !!

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
//            return false;
//
//    }


//    public void handleEvent(StausViewEvent message){
//        //return turn.isRequiredToMove() turn.isRequiredToBuild() turn.isAllowedToMove() turn.isAllowedToBuild()
//    }

    public void checkPlayer(ViewEvent message) {
        Turn turn = game.getTurn();

        Player viewPlayer = message.getPlayer();
        if(!turn.checkPlayer(viewPlayer)){
            //TODO
            //notify view about invalid player uuid
        }

    }
}
