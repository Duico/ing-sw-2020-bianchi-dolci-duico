package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.UndoViewEvent;


/**
 * defines special kind of response sent from controller to view when undo operation has failed
 * with a related reason
 */
public class FailedUndoControllerResponse extends ControllerResponse {
    private Reason reason;
    public FailedUndoControllerResponse(UndoViewEvent message, Reason reason) {
        super(message);
        this.reason = reason;
    }

    /**
     * defines particular reased that explains why undo operation failed
     */
    public enum Reason{
        NOT_AVAILABLE,
        TIMER_EXPIRED
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

