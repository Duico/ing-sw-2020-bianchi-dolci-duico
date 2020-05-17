package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.GameMessageVisitor;

public class CliGameMessageVisitor extends GameMessageVisitor {
    private Cli cli;
    public CliGameMessageVisitor(Cli cli, CliModelEventVisitor cliModelEventVisitor, CliControllerResponseVisitor cliControllerResponseVisitor, CliSetUpMessageVisitor cliSetUpMessageVisitor){
        //Cli property could be replaced by an event listener
        super(cliModelEventVisitor, cliControllerResponseVisitor, cliSetUpMessageVisitor);
        this.cli = cli;
    }

}
