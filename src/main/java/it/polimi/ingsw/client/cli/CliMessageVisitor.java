package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.MessageVisitor;

public class CliMessageVisitor extends MessageVisitor {
    public CliMessageVisitor(Cli cli, CliModel cliModel){
        //Cli property could be replaced by an event listener
        super(new CliModelEventVisitor(cli, cliModel), new CliControllerResponseVisitor(cli, cliModel), new CliSetUpMessageVisitor(cli, cliModel), new CliClientConnectionEventVisitor(cli));
    }

    /*@Override
    public void handleEvent(ModelEvent evt) {
        visit(evt);
    }

    @Override
    public void handleEvent(ControllerResponse evt) {
        visit(evt);
    }

    @Override
    public void handleEvent(SetUpMessage evt) {
        visit(evt);
    }*/
}
