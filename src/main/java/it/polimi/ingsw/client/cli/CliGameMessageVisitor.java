package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.GameMessageVisitor;

public class CliGameMessageVisitor extends GameMessageVisitor {
    private Cli cli;
    public CliGameMessageVisitor(Cli cli, CliModel cliModel){
        this.cli = cli;
        this.modelEventVisitor = new CliModelEventVisitor(cliModel);
        this.controllerResponseVisitor = new CliControllerResponseVisitor(cliModel);
        this.setUpMessageVisitor = new CliSetUpMessageVisitor(cli, cliModel);
    }

}
