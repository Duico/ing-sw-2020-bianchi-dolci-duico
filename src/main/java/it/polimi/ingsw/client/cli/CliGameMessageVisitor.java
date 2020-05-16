package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.SetUpMessage;

public class CliGameMessageVisitor extends GameMessageVisitor {
    public CliGameMessageVisitor(CliController cliController){
        this.modelEventVisitor = new CliModelEventVisitor(cliController);
        this.controllerResponseVisitor = new CliControllerResponseVisitor(cliController);
        this.setUpMessageVisitor = new CliSetUpMessageVisitor(cliController);
    }

}
