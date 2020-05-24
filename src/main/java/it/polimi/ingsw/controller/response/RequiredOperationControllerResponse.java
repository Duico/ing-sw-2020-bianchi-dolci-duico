
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.view.event.ViewEvent;

public class RequiredOperationControllerResponse extends ControllerResponse {
    private Operation operation;

    public RequiredOperationControllerResponse(ViewEvent event, Operation requiredOperation) {
        super(event);
        this.operation = requiredOperation;
    }

    public Operation getRequiredOperation() {
        return operation;
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}
