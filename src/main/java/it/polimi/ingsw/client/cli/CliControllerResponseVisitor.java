package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.controller.response.*;

public class CliControllerResponseVisitor implements ControllerResponseVisitor {
    private CliController cliController;
    public CliControllerResponseVisitor(CliController cliController){
        this.cliController = cliController;
    }

    @Override
    public void visit(FailedOperationControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("Failed operation"));
//        nextOperation();
    }

    @Override
    public void visit(FailedUndoControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("Failed undo"));
    }

    @Override
    public void visit(IllegalCardNameControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("illegal card name"));
    }

    @Override
    public void visit(IllegalCardNamesListControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("illegal list of cards"));
    }

    @Override
    public void visit(IllegalFirstPlayerControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("illegal first player"));
    }

    @Override
    public void visit(IllegalTurnPhaseControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("illegal turn phase"));
    }

    @Override
    public void visit(RequiredOperationControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("required operation"));
    }

    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("incorrectPlayer"));
    }

    @Override
    public void visit(SuccessControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("\nSuccessful operation"));
    }

    @Override
    public void visit(TurnInfoControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("Turn info. Is Allowed to Move: "+r.isAllowedToMove() + "...etc"));
    }
}
