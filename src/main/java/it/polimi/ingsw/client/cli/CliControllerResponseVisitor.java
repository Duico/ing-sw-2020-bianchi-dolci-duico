package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.controller.response.*;

public class CliControllerResponseVisitor extends ClientEventEmitter implements ControllerResponseVisitor {
    private Cli cli;
    private final CliModel cliModel;
    public CliControllerResponseVisitor(Cli cli, CliModel cliModel){
        this.cli = cli;
        this.cliModel = cliModel;
    }

    @Override
    public void visit(FailedOperationControllerResponse r) {
        cli.println(Color.YELLOW_BOLD.escape("Failed operation"));
//        nextOperation();
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
}
