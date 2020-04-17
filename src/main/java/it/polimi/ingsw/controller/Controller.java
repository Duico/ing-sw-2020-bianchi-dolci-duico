package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.event.*;


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
    private boolean checkWorkerNotPlaced(){
        Turn turn = game.getTurn();
        if(turn.isAnyWorkerNotPlaced()){
            //STOP
            //notify view
            return false;
        }
        return true;
    }

    public void endTurn(EndTurnViewEvent message){
        Turn turn = game.getTurn();
        RemoteView view = message.getView();
        if(!checkPlayer(view)){

        }
        if(checkWorkerNotPlaced()){
            //STOP
        }

        if(turn.isRequiredToMove()) {
            //notify view
        }

        if(turn.isRequiredToBuild()){
            //notify view
        }
        game.nextTurn();
    }

    public void challengerCards(CardsChallengerViewEvent message){
        Turn turn = game.getTurn();
        RemoteView view = message.getView();

        if(!checkPlayer(view)){
            //notify view
        }


        game.setChosenCards(message.getCards());
        game.nextTurn();
    }

    public void setPlayerCards(CardViewEvent message){
        Turn turn = game.getTurn();
        RemoteView view = message.getView();
        String cardName = message.getNameCard();
        if(!checkPlayer(view)){

        }

        game.setPlayerCards(cardName);
        game.nextTurn();
    }

    public void setFirstPlayer(FirstPlayerViewEvent message){
        Turn turn = game.getTurn();
        RemoteView view = message.getView();
        String nickName = message.getNickName();
        if(!checkPlayer(view)){

        }
        if(game.isSetFirstPlayer()){
            //notify view, già settato
        }

        game.setFirstPlayer(nickName);
        //the next turn is set in game.setFirstPlayer
    }

    public void move(MoveViewEvent message){
        Turn turn = game.getTurn();
        RemoteView view = message.getView();
        int currentWorkerId = message.getWorkerId();
        //CHECK player equals viewPlayer
        if(!checkPlayer(view)){
            //STOP
        }
        if(!checkWorkerId(message)){
            //STOP
            //wrong worker
        }

        Position destinationPosition = message.getDestinationPosition();

        if(checkWorkerNotPlaced()){
            //STOP
        }

        if(!turn.isAllowedToMove(currentWorkerId)){
            //STOP
            //notify view
        }

        //check block
        boolean possibleMove = true;

        if(turn.getPreviousBlockNextPlayer()) {
            if(game.isBlockedMove(currentWorkerId, destinationPosition)){
                possibleMove = false;

                //notify RemoteView
            }
        }
        //check isValidMove and isValidPush


        if(!game.isFeasibleMove(currentWorkerId, destinationPosition)){
            possibleMove = false;
            //notify view
        }



        if( possibleMove == true ) {

            game.move(currentWorkerId, destinationPosition);
            //notify view of success/failure
        }
    }

    public void place(PlaceViewEvent message){
        Turn turn = game.getTurn();
        RemoteView view = message.getView();
        if(!checkPlayer(view)){
            //STOP
        }

        if(!turn.isAnyWorkerNotPlaced()){

            //all workers are placed
            //notify view
        }
        int workerId = game.place(message.getDestinationPosition());
        if( workerId < 0){
            //notify view
        }
    }


    public void build(BuildViewEvent message){
        Turn turn = game.getTurn();
        RemoteView view = message.getView();
        int currentWorkerId = message.getWorkerId();
        if(!checkPlayer(view)){
            //STOP
        }
        if(!checkWorkerId(message)){
            //STOP
            //wrong worker selected
        }

        Position destinationPosition = message.getDestinationPosition();
        boolean isDome = message.isDome();

        if(checkWorkerNotPlaced()){
            //STOP
        }

        if(!turn.isAllowedToBuild(currentWorkerId)){

        }

        boolean possibleBuild = game.isFeasibleBuild(currentWorkerId, destinationPosition, isDome);
        if(possibleBuild){
            game.build(currentWorkerId, destinationPosition, isDome);

        }
    }

    public void undo(UndoViewEvent message){
        RemoteView view = message.getView();
        checkPlayer(view);
        //check 5 sec
        if(!game.undo()){
            //ERROR
            //notify view
        }
    }


//    public void handleEvent(StausViewEvent message){
//        //return turn.isRequiredToMove() turn.isRequiredToBuild() turn.isAllowedToMove() turn.isAllowedToBuild()
//    }

    private boolean checkPlayer(RemoteView view) {
        Turn turn = game.getTurn();
        Player viewPlayer = view.getPlayer();
        if(!turn.checkPlayer(viewPlayer)){
            //view.sendError();
            //TODO
            //notify view about invalid player uuid
            return false;
        }
        return true;
    }


//    private boolean updateWorkerId(WorkerViewEvent message){
//        Turn turn = game.getTurn();
//        int currentWorkerId = message.getWorkerId();
//        if(!turn.updateCurrentWorker(currentWorkerId)) {
//            //TODO
//            //notify view
//            return false;
//        }
//        return true;
//    }
    private boolean checkWorkerId(WorkerViewEvent message){
        Turn turn = game.getTurn();
        int currentWorkerId = message.getWorkerId();
        if(!turn.checkCurrentWorker(currentWorkerId)){
            //TODO
            //message contains an invalid workerId for the turn
            return false;
        }
        return true;
    }
}
