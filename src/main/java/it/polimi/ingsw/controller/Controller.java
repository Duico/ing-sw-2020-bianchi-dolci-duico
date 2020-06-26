
package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.event.*;

/**
 * main controlle class which controls any kind of operation requested by view
 */
public class Controller extends ControllerResponseEmitter implements GameViewEventListener {

    private Game game; //refer to our model

    public Controller(Game game) {
        this.game = game;
    }


    public void handleEvent(ViewEvent message) {
        System.out.println("Generic event from view");
    }

    /**
     * sends controller response to view containing informations about current turn
     * @param message message received from client
     */
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
    }

    /**
     * checks if current player is allowed to end his turn, eventually sends controller response
     * and udates game switching to next turn
     * @param message end turn event received from client
     */
    public void endTurn(EndTurnViewEvent message) {
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

    /**
     * checks if list of chosen cards received from view event is correct, sends controller response and
     * eventually updates game switching to next turn
     * @param message message received from client containing list of chosen cards
     */
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

    /**
     * checks if chosen card received from view event is correct, sends controller response and
     * eventually updates game switching to next turn
     * @param message message received from client containing chosen card
     */
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

    /**
     * checks if chosen first player received from view event is correct, sends controller response and
     * eventually updates game switching to normal turn
     * @param message message received from client containing first player chosen
     */
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

    /**
     * checks if move event received from view event is correct, sends controller response and
     * eventually updates main board
     * @param message message received from client containing a move operation
     */
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

    /**
     * checks if place worker event received from view event is correct, sends controller response and
     * eventually updates main board
     * @param message message received from client containing place position of a worker
     */
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

    /**
     * checks if build event received from view event is correct, sends controller response and
     * eventually updates main board
     * @param message message received from client containing a build operation
     */
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

    /**
     * checks if undo event received from view event is correct, sends controller response and
     * eventually updates main board
     * @param message message received from client containing an undo operation
     */
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

    /**
     * checks if a player which is not current player is trying to do an operation, then sends controller response
     * @param message message received from client generic operation
     */
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

    /**
     * checks if tried operation is allowed in current game's turnphase
     * @param message generic message received from client's view
     * @param turnPhase turn phase which allows operation tried by a player
     * @return true if operation tried is not allowed in current turnphase
     */
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