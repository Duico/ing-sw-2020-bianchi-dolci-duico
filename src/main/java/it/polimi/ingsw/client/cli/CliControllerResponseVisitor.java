package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.Operation;

public class CliControllerResponseVisitor extends ClientEventEmitter implements ControllerResponseVisitor {
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
        cli.println(Color.YELLOW_BOLD.escape("required operation"));
    }

    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("incorrectPlayer"));
    }

    @Override
    public void visit(SuccessControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("\nSuccessful operation"));
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
