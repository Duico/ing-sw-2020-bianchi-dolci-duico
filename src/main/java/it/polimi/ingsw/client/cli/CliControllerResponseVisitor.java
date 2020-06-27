package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.Operation;

/**
 * Class that implements the ControllerResponseVisitor
 */
public class CliControllerResponseVisitor extends ClientEventEmitter implements ControllerResponseVisitor {
    private Cli cli;
    private final CliModel cliModel;
    public CliControllerResponseVisitor(Cli cli, CliModel cliModel){
        this.cli = cli;
        this.cliModel = cliModel;
    }

    /**
     * Function which after a FailedOperationControllerResponse, based on FailedOperationControllerResponse.Reason
     * prints all the information about the current turn with an info messages
     * @param r
     */
    @Override
    public void visit(FailedOperationControllerResponse r) {
        if(r.getOperation().equals(Operation.PLACE)){
            if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_FEASIBLE)){
               printAll(CliText.WORKERS_ALREADY_PLACE.toString());
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.DESTINATION_NOT_EMPTY)){
                printAll(CliText.PLACE_DESTINATION_NOT_EMPTY.toString());
            }
        }else if(r.getOperation().equals(Operation.MOVE)){
            if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER)){
                printAll(CliText.NOT_CURRENT_WORKER.toString());
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_ALLOWED)){
                printAll(CliText.NOT_ALLOWED_TO_MOVE.toString());
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.BLOCKED_BY_OPPONENT)){
                printAll(CliText.BLOCKED_MOVE.toString());
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_FEASIBLE)){
                printAll(CliText.NOT_FEASIBLE_MOVE.toString());
            }

        } else if(r.getOperation().equals(Operation.BUILD)){
            if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER)){
                printAll(CliText.NOT_CURRENT_WORKER.toString());
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_ALLOWED)){
                printAll(CliText.NOT_ALLOWED_TO_BUILD.toString());
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_FEASIBLE)){
                printAll(CliText.NOT_FEASIBLE_BUILD.toString());
            }

        }
    }

    /**
     * Function which after a FailedUndoControllerResponse, prints all the information about the current turn
     * with an info message (undo not available)
     * @param r
     */
    @Override
    public void visit(FailedUndoControllerResponse r) {
        printAll(CliText.UNDO_NOT_AVAILABLE.toString());
    }

    /**
     * Function which after a IllegalCardNameControllerResponse,
     * prints a message (illegal card name)
     * @param r
     */
    @Override
    public void visit(IllegalCardNameControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("illegal card name"));
    }

    /**
     * Function which after a IllegalCardNamesListControllerResponse,
     * prints a message (illegal list of cards)
     * @param r
     */
    @Override
    public void visit(IllegalCardNamesListControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("illegal list of cards"));

    }

    /**
     * Function which after a IllegalFirstPlayerControllerResponse,
     * prints a message (illegal first player)
     * @param r
     */
    @Override
    public void visit(IllegalFirstPlayerControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("illegal first player"));
    }

    /**
     * Function which after a IllegalTurnPhaseControllerResponse,
     * prints all the informations about the current turn with an info message (illegal turn phase)
     * @param r
     */
    @Override
    public void visit(IllegalTurnPhaseControllerResponse r) {
        printAll(CliText.ILLEGAL_TURN_PHASE.toString());
    }


    /**
     * Function which after a RequiredOperationControllerResponse, based on the type of operation
     * prints all the informations about the current turn with an info message (the type of operation required)
     * @param r
     */
    @Override
    public void visit(RequiredOperationControllerResponse r) {
        if(r.getRequiredOperation().equals(Operation.MOVE)){
            printAll(CliText.REQUIRED_MOVE.toString());
        } else if(r.getRequiredOperation().equals(Operation.BUILD)){
            printAll(CliText.REQUIRED_BUILD.toString());
        } else if(r.getRequiredOperation().equals(Operation.PLACE)){
            printAll(CliText.REQUIRED_PLACE.toString());
        }

    }

    /**
     * Function which after a NotCurrentPlayerControllerResponse
     * prints all the informations about the current turn with an info message (not current player)
     * @param r
     */
    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {
        printAll(CliText.NOT_CURRENT_PLAYER.toString());
    }

    @Override
    public void visit(SuccessControllerResponse r) {

    }

    @Override
    public void visit(TurnInfoControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("Turn info. Is Allowed to Move: "+r.isAllowedToMove() + "...etc"));
    }

    private void printAll(){
        printAll(cliModel.isMyTurn(), cliModel.getLastInfoMessage());
    }
    private void printAll(boolean myTurn){
        printAll(myTurn, cliModel.getLastInfoMessage());
    }
    private void printAll(String infoMessage){
        printAll(cliModel.isMyTurn(), infoMessage);
    }

    /**
     * Function which set LastInfoMessage in cliModel and prints all the informations about
     * the current turn
     * @param myTurn
     * @param infoMessage
     */
    private void printAll(boolean myTurn, String infoMessage) {
        cliModel.setLastInfoMessage(infoMessage);
        BoardPrinter bp = cliModel.createBoardPrinter();
        cli.printAll(bp, myTurn, infoMessage, false);

    }
}
