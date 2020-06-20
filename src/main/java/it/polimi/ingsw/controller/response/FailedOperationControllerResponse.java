package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.view.event.WorkerViewEvent;

public class FailedOperationControllerResponse extends ControllerResponse {
    private Operation operation;
    private Reason reason;

    public FailedOperationControllerResponse(WorkerViewEvent message, Operation failedOperation, Reason reason) {
        super(message);
        this.operation = failedOperation;
        this.reason = reason;
    }

    public Operation getOperation() {
        return operation;
    }

    public enum Reason{
        NOT_ALLOWED,
        BLOCKED_BY_OPPONENT,
        NOT_FEASIBLE,
        NOT_CURRENT_WORKER,
        DESTINATION_NOT_EMPTY;
    }

    public Reason getReason(){
        return reason;
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

