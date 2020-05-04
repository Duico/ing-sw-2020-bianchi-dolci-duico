
package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.*;

import java.util.List;


public class Controller implements GameViewEventListener {

    private Game game; //refer to our model

    public Controller(Game game) {
        this.game = game;
    }


    public void handleEvent(ViewEvent message) {
        System.out.println("Generic event from view");
    }

    /*public void sendInfo(InfoViewEvent message){
        RemoteView view = message.getView();
        //Player player = view.getPlayer();
        //IMPLEMENT IF NEEDED
        // send REQUIRED info about the player
        // like:
        // Required/Allowed Operations (ONLY THESE if a flag is set)
        // turn Phase, workers to place
        // undoAvailable
        // etc
    }*/

    public void endTurn(EndTurnViewEvent message){
        RemoteView view = message.getView();
        if(checkIsWrongPlayer(message)){
            return;
        }
        boolean advanceTurn = true;
        if(game.isRequiredToMove()) {
            view.sendMessage("you have to move a worker");
            view.eventResponse(new RequiredOperationControllerResponse(message, Operation.MOVE));
            advanceTurn = false;
        }

        if(game.isRequiredToBuild()) {
            view.sendMessage("you have to build");
            view.eventResponse(new RequiredOperationControllerResponse(message, Operation.BUILD));
            advanceTurn = false;
        }

        if(game.isAnyWorkerNotPlaced()){
            view.sendMessage("you have to set the worker on the board");
            advanceTurn = false;
        }

        if(advanceTurn) {
            view.eventResponse(new SuccessControllerResponse(message));
            view.sendMessage("correct end turn");
            game.nextTurn();
        }
    }

    public void challengerCards(ChallengerCardViewEvent message){
        RemoteView view = message.getView();

        if(checkIsWrongPlayer(message)){
            view.sendMessage("incorrect player");
            return;
        }


        if(!game.setChosenCards(message.getCardNamesList())){

            view.sendMessage("wrong name Cards, the number of cards is incorrect");
            view.eventResponse(new IllegalCardNamesListControllerResponse(message));
        }else{
            //view.sendMessage("correct Cards");
            view.eventResponse(new SuccessControllerResponse(message));
            game.nextTurn();
        }
    }

    public void setPlayerCard(CardViewEvent message){
        RemoteView view = message.getView();
        String cardName = message.getCardName();
        if(checkIsWrongPlayer(message)){
            return;
        }

        if(!game.setPlayerCard(cardName)){
            view.sendMessage("incorrect name Card");
            List<String> cardNames = game.getChosenCardsNames();
            view.eventResponse(new IllegalCardNameControllerResponse(message, cardNames));
            return;
        }
        view.eventResponse(new SuccessControllerResponse(message));
        //game.nextTurn();
    }

    public void setFirstPlayer(FirstPlayerViewEvent message){
        RemoteView view = message.getView();
        //Player firstPlayer = message.getFirstPlayer();
        if(checkIsWrongPlayer(message)){
            return;
        }
        if(!isCurrentPlayerChalleger()){
            view.sendMessage("Player is not challenger");
            view.eventResponse(new NotCurrentPlayerControllerResponse(message));
            return;
        }
        if(checkIsWrongTurnPhase(message, TurnPhase.CHOSE_CARDS)){
            return;
        }

        if(game.isSetFirstPlayer()){
            view.sendMessage("Player already set");
            view.eventResponse(new IllegalFirstPlayerControllerResponse(message, IllegalFirstPlayerControllerResponse.Reason.ALREADY_SET));
            return;
        }
            //check all workers are set and all cards are set
            //^^DONE
            //^^wrongTurnPhase should do

        //the next turn is set in game.setFirstPlayer
        if(!game.firstTurn(message.getFirstPlayer())){
            view.sendMessage("incorrect setting");
            view.eventResponse(new IllegalFirstPlayerControllerResponse(message, IllegalFirstPlayerControllerResponse.Reason.NON_EXISTENT));
        }else {
            //view.sendMessage("correct setting");
            view.eventResponse(new SuccessControllerResponse(message));
        }
    }

    public void move(MoveViewEvent message){
        System.out.println("controller");
        RemoteView view = message.getView();
        Position workerPosition = message.getWorkerPosition();


        if(checkIsWrongPlayer(message)){
            view.sendMessage("incorrect player");
            return;
        }
        if(!isCurrentWorkerId(message)){
            view.sendMessage("incorrect Worker");
            view.eventResponse(new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER));
            return;
        }

        Position destinationPosition = message.getDestinationPosition();

        if(checkIsWrongTurnPhase(message, TurnPhase.NORMAL)){
            view.sendMessage("incorrect turn phase");
            return;
        }

        if(!game.isAllowedToMove(workerPosition)){
            view.sendMessage("you are not allowed to move");
            view.eventResponse(new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.NOT_ALLOWED));
            return;
        }

        //checks block
        if(game.isBlockedMove(workerPosition, destinationPosition)){
            view.sendMessage("it is a block movement");
            view.eventResponse(new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.BLOCKED_BY_OPPONENT));
            return;
        }

        //checks isValidMove and isValidPush
        if(!game.isFeasibleMove(workerPosition, destinationPosition)){
            view.sendMessage("is not a feasible movement");
            view.eventResponse(new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.NOT_FEASIBLE));
            return;
        }

        view.eventResponse(new SuccessControllerResponse(message));
        game.move(workerPosition, destinationPosition);
        view.sendMessage("Successfully movement");

    }

    public void place(PlaceViewEvent message){
        RemoteView view = message.getView();
        if(checkIsWrongPlayer(message)){
            view.sendMessage("incorrectPlayer");
            return;
        }

        if(checkIsWrongTurnPhase(message, TurnPhase.PLACE_WORKERS)){
            view.sendMessage("incorrect turn phase");
            return;
        }

        if(!game.isAnyWorkerNotPlaced()){
            view.sendMessage("You have already place all the workers");
            view.eventResponse(new FailedOperationControllerResponse(message, Operation.PLACE, FailedOperationControllerResponse.Reason.NOT_FEASIBLE));
        }



        //int workerId = game.place(message.getDestinationPosition());
        int workerId = game.place(message.getWorkerPosition());
        if( workerId < 0){
            view.sendMessage("incorrect position");
            view.eventResponse(new FailedOperationControllerResponse(message, Operation.PLACE, FailedOperationControllerResponse.Reason.DESTINATION_NOT_EMPTY));
        }else{
            //view.sendMessage("correct position, worker set");
            view.eventResponse(new SuccessControllerResponse(message));
        }
    }


    public void build(BuildViewEvent message){
        RemoteView view = message.getView();
        Position workerPosition = message.getWorkerPosition();

        if(checkIsWrongPlayer(message)){
            view.sendMessage("incorrect player");
            return;
        }
        if(!isCurrentWorkerId(workerPosition)){
            view.sendMessage("incorrect Worker");
            view.eventResponse(new FailedOperationControllerResponse(message, Operation.BUILD, FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER));
            return;
        }

        Position destinationPosition = message.getDestinationPosition();
        boolean isDome = message.isDome();

        if(checkIsWrongTurnPhase(message, TurnPhase.NORMAL)){
            view.sendMessage("incorrect turn phase");
            return;
        }

        if(!game.isAllowedToBuild()){
            view.sendMessage("you are not allowed to build");
            view.eventResponse(new FailedOperationControllerResponse(message, Operation.BUILD, FailedOperationControllerResponse.Reason.NOT_ALLOWED));
            return;
        }



        if(!game.isFeasibleBuild(workerPosition, destinationPosition, isDome)){
            view.sendMessage("is not a feasible build");
            view.eventResponse(new FailedOperationControllerResponse(message, Operation.BUILD, FailedOperationControllerResponse.Reason.NOT_FEASIBLE));
            return;
        }
        view.eventResponse(new SuccessControllerResponse(message));
        game.build(workerPosition, destinationPosition, isDome);
        view.sendMessage("correct build");


    }

    public void undo(UndoViewEvent message){
        RemoteView view = message.getView();
        if(checkIsWrongPlayer(message)){
            view.sendMessage("incorrect player");
            return;
        }
        //TODO
        //timer 5 sec
        if(!game.undo()){
            view.sendMessage("unsuccess undo");
            view.eventResponse(new FailedUndoControllerResponse(message, FailedUndoControllerResponse.Reason.NOT_AVAILABLE));
        }else{
            view.sendMessage("successfully undo");
            view.eventResponse(new SuccessControllerResponse(message));
        }
    }

    private boolean checkIsWrongPlayer(ViewEvent message) {
        RemoteView view = message.getView();
        Player viewPlayer = view.getPlayer();
        if(game.getCurrentPlayer().getUuid().equals(viewPlayer.getUuid())){
            view.eventResponse(new NotCurrentPlayerControllerResponse(message));
            return false;
        }
        return true;
    }

    private boolean isCurrentPlayerChalleger(){
        //you always have to call checkPlayer before
        return game.getCurrentPlayer().isChallenger();
    }

    private boolean checkIsWrongTurnPhase(ViewEvent message, TurnPhase turnPhase){
        RemoteView view = message.getView();
        if(game.getTurnPhase() != turnPhase){
            view.eventResponse(new IllegalTurnPhaseControllerResponse(message, turnPhase));
            return true;
        }
        return false;
    }
    private boolean isCurrentWorkerId(WorkerViewEvent message){
        Position currentWorkerPosition = message.getWorkerPosition();
        if(!game.checkCurrentWorker(currentWorkerPosition)){
            return false;
        }
        return true;
    }

    private boolean isCurrentWorkerId(Position currentWorkerPosition){
        //Position currentWorkerPosition = message.getWorkerPosition();
        if(!game.checkCurrentWorker(currentWorkerPosition)){
            return false;
        }
        return true;
    }


}

