package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.ControllerResponseVisitor;
import it.polimi.ingsw.client.cli.ModelEventVisitor;
import it.polimi.ingsw.client.cli.SetUpMessageVisitor;
import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.SetUpMessage;

public abstract class GameMessageVisitor {
    protected ModelEventVisitor modelEventVisitor;
    protected ControllerResponseVisitor controllerResponseVisitor;
    protected SetUpMessageVisitor setUpMessageVisitor;

    public void visit(ModelEvent evt) {
        evt.accept(modelEventVisitor);
    }

    public void visit(ControllerResponse evt) {
        evt.accept(controllerResponseVisitor);
    }

    public void visit(SetUpMessage evt) {
        evt.accept(setUpMessageVisitor);
    }
}