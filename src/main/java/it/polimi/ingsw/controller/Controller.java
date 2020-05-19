
package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.RemoteView;
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

    public void requiredTurnInfo(InfoViewEvent message){
        RemoteView view = message.getView();
        boolean isRequiredToMove = game.isRequiredToMove();
        boolean isRequiredToBuild = game.isRequiredToBuild();
        boolean isAllowedToMove = game.isAllowedToMove();
        boolean isAllowedToBuild = game.isAllowedToBuild();
        boolean isUndoAvailable = game.isUndoAvailable();
        ControllerResponse response = new TurnInfoControllerResponse(message, isRequiredToMove, isRequiredToBuild, isAllowedToMove, isAllowedToBuild, isUndoAvailable);
        view.eventResponse(response);
        //Player player = view.getPlayer();
        //IMPLEMENT IF NEEDED
        // send REQUIRED info about the player
        // like:
        // Required/Allowed Operations (ONLY THESE if a flag is set)
        // turn Phase, workers to place
        // undoAvailable
        // etc
    }

    public void endTurn(EndTurnViewEvent message){
        RemoteView view = message.getView();
        if(checkIsWrongPlayer(message)){
            return;
        }

        boolean advanceTurn = true;
        if(game.isRequiredToMove()) {
            view.sendMessage("you have to move a worker");
            ControllerResponse response = new RequiredOperationControllerResponse(message, Operation.MOVE);
            view.eventResponse(response);
            advanceTurn = false;
            return;
        }

        if(game.isRequiredToBuild()) {
            view.sendMessage("you have to build");
            ControllerResponse response = new RequiredOperationControllerResponse(message, Operation.BUILD);
            view.eventResponse(response);
            advanceTurn = false;
            return;
        }

        if(game.isAnyWorkerNotPlaced()){
            view.sendMessage("you have to set the worker on the board");
            ControllerResponse response = new RequiredOperationControllerResponse(message, Operation.PLACE);
            view.eventResponse(response);
            advanceTurn = false;
            return;
        }

        if(advanceTurn) {
            ControllerResponse response = new SuccessControllerResponse(message);
            view.eventResponse(response);
            view.sendMessage("correct end turn");
            game.nextTurn();
        }
    }

    public void challengerCards(ChallengerCardViewEvent message){
        RemoteView view = message.getView();

        if(checkIsWrongPlayer(message)){
            return;
        }


        if(!game.setChosenCards(message.getCardNamesList())){
            view.sendMessage("wrong name Cards, the number of cards is incorrect");
            ControllerResponse response = new IllegalCardNamesListControllerResponse(message);
            view.eventResponse(response);
        }else{
            view.sendMessage("correct Cards");
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
            /*ControllerResponse response = new IllegalCardNameControllerResponse(message, cardNames);
            view.eventResponse(response);*/
            return;
        }
        //view.eventResponse(new SuccessControllerResponse(message));

        //Put back here again
        game.nextTurn();
    }

    public void setFirstPlayer(FirstPlayerViewEvent message){
        RemoteView view = message.getView();
        //Player firstPlayer = message.getFirstPlayer();
        if(checkIsWrongPlayer(message)){
            return;
        }
        if(!isCurrentPlayerChallenger()){
            view.sendMessage("Player is not challenger");
            view.eventResponse(new NotCurrentPlayerControllerResponse(message));
            return;
        }
        if(checkIsWrongTurnPhase(message, TurnPhase.CHOSE_CARDS)){
            return;
        }

        if(game.isSetFirstPlayer()){
            view.sendMessage("Player already set");
            ControllerResponse response = new IllegalFirstPlayerControllerResponse(message, IllegalFirstPlayerControllerResponse.Reason.ALREADY_SET);
            view.eventResponse(response);
            return;
        }
            //check all workers are set and all cards are set
            //^^DONE
            //^^wrongTurnPhase should do

        //the next turn is set in game.firstTurn
        if(!game.firstTurn(message.getFirstPlayer())){
            view.sendMessage("incorrect setting");
            ControllerResponse response = new IllegalFirstPlayerControllerResponse(message, IllegalFirstPlayerControllerResponse.Reason.NON_EXISTENT);
            view.eventResponse(response);
        }else {
            //view.sendMessage("correct setting");
            //view.eventResponse(new SuccessControllerResponse(message));
        }
    }

    public void move(MoveViewEvent message){

        RemoteView view = message.getView();
        Position workerPosition = message.getWorkerPosition();


        if(checkIsWrongPlayer(message)){
            view.sendMessage("incorrect player");
            return;
        }
        if(checkIsWrongTurnPhase(message, TurnPhase.NORMAL)){
            view.sendMessage("incorrect turn phase");
            return;
        }
        if(!isCurrentWorkerId(message)){
            view.sendMessage("incorrect Worker");
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER);
            view.eventResponse(response);
            return;
        }

        Position destinationPosition = message.getDestinationPosition();


        if(!game.isAllowedToMove(workerPosition)){
            view.sendMessage("you are not allowed to move");
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.NOT_ALLOWED);
            view.eventResponse(response);
            return;
        }

        //checks block
        if(game.isBlockedMove(workerPosition, destinationPosition)){
            view.sendMessage("it is a block movement");
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.BLOCKED_BY_OPPONENT);
            view.eventResponse(response);
            return;
        }

        //checks isValidMove and isValidPush
        if(!game.isFeasibleMove(workerPosition, destinationPosition)){
            view.sendMessage("is not a feasible movement");
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.NOT_FEASIBLE);
            view.eventResponse(response);
            return;
        }

        //view.eventResponse(new SuccessControllerResponse(message));
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
            return;
        }



        //int workerId = game.place(message.getDestinationPosition());
        int workerId = game.place(message.getWorkerPosition());
        if( workerId < 0){
            view.sendMessage("incorrect position");
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.PLACE, FailedOperationControllerResponse.Reason.DESTINATION_NOT_EMPTY);
            view.eventResponse(response);
        }else{
            //view.sendMessage("correct position, worker set");
            //view.eventResponse(new SuccessControllerResponse(message));
        }
    }


    public void build(BuildViewEvent message){
        RemoteView view = message.getView();
        Position workerPosition = message.getWorkerPosition();

        if(checkIsWrongPlayer(message)){
            view.sendMessage("incorrect player");
            return;
        }
        if(checkIsWrongTurnPhase(message, TurnPhase.NORMAL)){
            view.sendMessage("incorrect turn phase");
            return;
        }
        if(!isCurrentWorkerId(workerPosition)){
            view.sendMessage("incorrect Worker");
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.BUILD, FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER);
            view.eventResponse(response);
            return;
        }

        Position destinationPosition = message.getDestinationPosition();
        boolean isDome = message.isDome();

        if(!game.isAllowedToBuild()){
            view.sendMessage("you are not allowed to build");
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.BUILD, FailedOperationControllerResponse.Reason.NOT_ALLOWED);
            view.eventResponse(response);
            return;
        }



        if(!game.isFeasibleBuild(workerPosition, destinationPosition, isDome)){
            view.sendMessage("is not a feasible build");
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.BUILD, FailedOperationControllerResponse.Reason.NOT_FEASIBLE);
            view.eventResponse(response);
            return;
        }
        //view.eventResponse(new SuccessControllerResponse(message));
        game.build(workerPosition, destinationPosition, isDome);
        view.sendMessage("correct build");


    }

    public void undo(UndoViewEvent message){
        RemoteView view = message.getView();
        if(checkIsWrongPlayer(message)){
            ControllerResponse response = new NotCurrentPlayerControllerResponse(message);
            view.eventResponse(response);
            view.sendMessage("incorrect player");
            return;
        }
        //TODO
        //timer 5 sec
        if(!game.undo()){
            view.sendMessage("unsuccess undo");
            ControllerResponse response = new FailedUndoControllerResponse(message, FailedUndoControllerResponse.Reason.NOT_AVAILABLE);
            view.eventResponse(response);
        }else{
            view.sendMessage("successfully undo");
            //view.eventResponse(new SuccessControllerResponse(message));
        }
    }

    private boolean checkIsWrongPlayer(ViewEvent message) {
        RemoteView view = message.getView();
        Player viewPlayer = view.getPlayer();
        if(game.getCurrentPlayer().getUuid().equals(viewPlayer.getUuid())) {
            return false;
        }
        ControllerResponse response = new NotCurrentPlayerControllerResponse(message);
        view.eventResponse(response);
        return true;
    }

    private boolean isCurrentPlayerChallenger(){
        //you always have to call checkPlayer before
        return game.getCurrentPlayer().isChallenger();
    }

    private boolean checkIsWrongTurnPhase(ViewEvent message, TurnPhase turnPhase){
        RemoteView view = message.getView();
        if(game.getTurnPhase() != turnPhase){
            ControllerResponse response = new IllegalTurnPhaseControllerResponse(message, TurnPhase.NORMAL);
            view.eventResponse(response);
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

