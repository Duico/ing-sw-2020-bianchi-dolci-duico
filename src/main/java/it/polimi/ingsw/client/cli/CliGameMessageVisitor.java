package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.GameMessage;
import it.polimi.ingsw.server.message.SetUpMessage;

public class CliGameMessageVisitor extends GameMessageVisitor {
    protected Cli cli;
    public CliGameMessageVisitor(Cli cli, CliModelEventVisitor cliModelEventVisitor, CliControllerResponseVisitor cliControllerResponseVisitor, CliSetUpMessageVisitor cliSetUpMessageVisitor){
        super(cliModelEventVisitor, cliControllerResponseVisitor, cliSetUpMessageVisitor);
        this.cli = cli;

    }

    @Override
    public void handleEvent(ClientConnectionEvent evt) {

        cli.printClientConnectionEvent(evt);
    }

    @Override
    public void handleEvent(GameMessage evt) {
        evt.accept(this);
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
