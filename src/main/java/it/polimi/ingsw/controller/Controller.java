
package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.event.*;

import java.util.List;


public class Controller extends ControllerResponseEmitter implements GameViewEventListener {

    private Game game; //refer to our model

    public Controller(Game game) {
        this.game = game;
    }


    public void handleEvent(ViewEvent message) {
        System.out.println("Generic event from view");
    }

    public void requiredTurnInfo(InfoViewEvent message) {
        //needed to avoid the wrong player receiving currentPlayer's answer
        if (checkIsWrongPlayer(message)) {
            return;
        }
        boolean isRequiredToMove = game.isRequiredToMove();
        boolean isRequiredToBuild = game.isRequiredToBuild();
        boolean isAllowedToMove = game.isAllowedToMove();
        boolean isAllowedToBuild = game.isAllowedToBuild();
        boolean isUndoAvailable = game.isUndoAvailable();
        ControllerResponse response = new TurnInfoControllerResponse(message, isRequiredToMove, isRequiredToBuild, isAllowedToMove, isAllowedToBuild, isUndoAvailable);
        emitEvent(response);
        //Player player = view.getPlayer();
        //IMPLEMENT IF NEEDED
        // send REQUIRED info about the player
        // like:
        // Required/Allowed Operations (ONLY THESE if a flag is set)
        // turn Phase, workers to place
        // undoAvailable
        // etc
    }

    public void endTurn(EndTurnViewEvent message) {
        System.out.println("Il player è " + message.getPlayer());
        if (checkIsWrongPlayer(message)) {
            return;
        }

        boolean advanceTurn = true;
        if (game.isRequiredToMove()) {
            ControllerResponse response = new RequiredOperationControllerResponse(message, Operation.MOVE);
            emitEvent(response);
            return;
        }

        if (game.isRequiredToBuild()) {
            ControllerResponse response = new RequiredOperationControllerResponse(message, Operation.BUILD);
            emitEvent(response);
            return;
        }

        if (game.isAnyWorkerNotPlaced()) {
            ControllerResponse response = new RequiredOperationControllerResponse(message, Operation.PLACE);
            emitEvent(response);
            return;
        }

        ControllerResponse response = new SuccessControllerResponse(message);
        emitEvent(response);
        game.nextTurn();
    }

    public void challengerCards(ChallengerCardViewEvent message) {
        if (checkIsWrongPlayer(message)) {
            return;
        }

        if (!game.setChosenCards(message.getCardNamesList())) {
            ControllerResponse response = new IllegalCardNamesListControllerResponse(message);
            emitEvent(response);
        } else {
            emitEvent(new SuccessControllerResponse(message));
            game.nextTurn();
        }
    }

    public void setPlayerCard(CardViewEvent message) {
        String cardName = message.getCardName();
        if (checkIsWrongPlayer(message)) {
            return;
        }

        if (!game.setPlayerCard(cardName)) {
            return;
        }
        game.nextTurn();
    }

    public void setFirstPlayer(FirstPlayerViewEvent message) {
        if (checkIsWrongPlayer(message)) {
            return;
        }
        if (!isCurrentPlayerChallenger()) {
            emitEvent(new NotCurrentPlayerControllerResponse(message));
            return;
        }
        if (checkIsWrongTurnPhase(message, TurnPhase.CHOSE_CARDS)) {
            return;
        }

        if (game.isSetFirstPlayer()) {
            ControllerResponse response = new IllegalFirstPlayerControllerResponse(message, IllegalFirstPlayerControllerResponse.Reason.ALREADY_SET);
            emitEvent(response);
            return;
        }
        //the next turn is set in game.firstTurn
        if (!game.firstTurn(message.getFirstPlayer())) {
            ControllerResponse response = new IllegalFirstPlayerControllerResponse(message, IllegalFirstPlayerControllerResponse.Reason.NON_EXISTENT);
            emitEvent(response);
        } else {
            //view.eventResponse(new SuccessControllerResponse(message));
        }
    }

    public void move(MoveViewEvent message) {
        Position workerPosition = message.getWorkerPosition();

        if (checkIsWrongPlayer(message)) {
            return;
        }
        if (checkIsWrongTurnPhase(message, TurnPhase.NORMAL)) {
            return;
        }
        if (!isCurrentWorkerId(workerPosition)) {
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER);
            emitEvent(response);
            return;
        }

        Position destinationPosition = message.getDestinationPosition();

        if (!game.isAllowedToMove(workerPosition)) {
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.NOT_ALLOWED);
            emitEvent(response);
            return;
        }
        if (game.isBlockedMove(workerPosition, destinationPosition)) {
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.BLOCKED_BY_OPPONENT);
            emitEvent(response);
            return;
        }

        if (!game.isFeasibleMove(workerPosition, destinationPosition)) {
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.MOVE, FailedOperationControllerResponse.Reason.NOT_FEASIBLE);
            emitEvent(response);
            return;
        }
        game.move(workerPosition, destinationPosition);

    }

    public void place(PlaceViewEvent message) {
        if (checkIsWrongPlayer(message)) {
            return;
        }

        if (checkIsWrongTurnPhase(message, TurnPhase.PLACE_WORKERS)) {
            return;
        }

        if (!game.isAnyWorkerNotPlaced()) {
            emitEvent(new FailedOperationControllerResponse(message, Operation.PLACE, FailedOperationControllerResponse.Reason.NOT_FEASIBLE));
            return;
        }

        Optional<Integer> workerId = game.place(message.getWorkerPosition());
        if (!workerId.isPresent()) {
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.PLACE, FailedOperationControllerResponse.Reason.DESTINATION_NOT_EMPTY);
            emitEvent(response);
        } else {
        }
    }


    public void build(BuildViewEvent message) {
        Position workerPosition = message.getWorkerPosition();

        if (checkIsWrongPlayer(message)) {
            return;
        }
        if (checkIsWrongTurnPhase(message, TurnPhase.NORMAL)) {
            return;
        }
        if (!isCurrentWorkerId(workerPosition)) {
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.BUILD, FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER);
            emitEvent(response);
            return;
        }

        Position destinationPosition = message.getDestinationPosition();
        boolean isDome = message.isDome();

        if (!game.isAllowedToBuild()) {
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.BUILD, FailedOperationControllerResponse.Reason.NOT_ALLOWED);
            emitEvent(response);
            return;
        }


        if (!game.isFeasibleBuild(workerPosition, destinationPosition, isDome)) {
            ControllerResponse response = new FailedOperationControllerResponse(message, Operation.BUILD, FailedOperationControllerResponse.Reason.NOT_FEASIBLE);
            emitEvent(response);
            return;
        }
        game.build(workerPosition, destinationPosition, isDome);

    }

    public void undo(UndoViewEvent message) {
        if (checkIsWrongPlayer(message)) {
            ControllerResponse response = new NotCurrentPlayerControllerResponse(message);
            emitEvent(response);
            return;
        }
        if (!game.undo()) {
            ControllerResponse response = new FailedUndoControllerResponse(message, FailedUndoControllerResponse.Reason.NOT_AVAILABLE);
            emitEvent(response);
        } else {
            //view.eventResponse(new SuccessControllerResponse(message));
        }
    }

    private boolean checkIsWrongPlayer(ViewEvent message) {
        Player viewPlayer = message.getPlayer();
        if (game.getCurrentPlayer().getUuid().equals(viewPlayer.getUuid())) {
            return false;
        }
        ControllerResponse response = new NotCurrentPlayerControllerResponse(message);
        emitEvent(response);
        return true;
    }

    private boolean isCurrentPlayerChallenger() {
        //you always have to call checkPlayer before
        return game.getCurrentPlayer().isChallenger();
    }

    private boolean checkIsWrongTurnPhase(ViewEvent message, TurnPhase turnPhase) {
        if (game.getTurnPhase() != turnPhase) {
            ControllerResponse response = new IllegalTurnPhaseControllerResponse(message, TurnPhase.NORMAL);
            emitEvent(response);
            return true;
        }
        return false;
    }

    private boolean isCurrentWorkerId(Position currentWorkerPosition) {
        if (!game.checkCurrentWorker(currentWorkerPosition)) {
            return false;
        }
        return true;
    }
}