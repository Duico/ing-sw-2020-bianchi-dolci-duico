package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.GameMessageVisitor;

public class CliGameMessageVisitor extends GameMessageVisitor {
    public CliGameMessageVisitor(Cli cli, CliModel cliModel){
        //Cli property could be replaced by an event listener
        super(new CliModelEventVisitor(cli, cliModel), new CliControllerResponseVisitor(cli, cliModel), new CliSetUpMessageVisitor(cli, cliModel));
    }

}
