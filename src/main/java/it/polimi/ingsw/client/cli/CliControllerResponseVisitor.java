package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.TurnPhase;

public class CliControllerResponseVisitor extends ControllerResponseVisitor {
    private Cli cli;
    private final CliModel cliModel;
    public CliControllerResponseVisitor(Cli cli, CliModel cliModel){
        this.cli = cli;
        this.cliModel = cliModel;
    }

    @Override
    public void visit(FailedOperationControllerResponse r) {
        //cli.println(Color.YELLOW_BOLD.escape("Failed operation"));
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

    @Override
    public void visit(FailedUndoControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("Failed undo"));
    }

    @Override
    public void visit(IllegalCardNameControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("illegal card name"));
    }

    @Override
    public void visit(IllegalCardNamesListControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("illegal list of cards"));
    }

    @Override
    public void visit(IllegalFirstPlayerControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("illegal first player"));
    }

    @Override
    public void visit(IllegalTurnPhaseControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("illegal turn phase"));
    }

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

    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("incorrectPlayer"));
    }

    @Override
    public void visit(SuccessControllerResponse r) {
//        if(cliModel.getTurnPhase().equals(TurnPhase.CHOSE_CARDS)) {
//            cli.println(System.lineSeparator() + CliText.SUCCESSFUL_OPERATION.toString());
//        }else{
////            printAll(CliText.SUCCESSFUL_OPERATION.toString());
//        }
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
    private void printAll(boolean myTurn, String infoMessage) {
//        return () -> {
        cliModel.setLastInfoMessage(infoMessage);
        BoardPrinter bp = cliModel.createBoardPrinter();
        cli.printAll(bp, myTurn, infoMessage);
        //command is read by another thread
//        };
    }
}
