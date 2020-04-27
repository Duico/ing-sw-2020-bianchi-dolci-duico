package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.view.event.GameViewEvent;
import it.polimi.ingsw.view.event.ViewEvent;

public class RequiredOperationControllerResponse extends ControllerResponse {
    private Operation operation;

    public RequiredOperationControllerResponse(GameViewEvent event, Operation requiredOperation) {
        super(event);
        this.operation = requiredOperation;
    }

    public Operation getRequiredOperation() {
        return operation;
    }
}
