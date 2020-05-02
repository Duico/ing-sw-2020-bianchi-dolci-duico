package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.view.event.UndoViewEvent;

public class FailedUndoControllerResponse extends ControllerResponse {
    private Reason reason;
    public FailedUndoControllerResponse(UndoViewEvent message, Reason reason) {
        super(message);
        this.reason = reason;
    }
    public enum Reason{
        NOT_AVAILABLE,
        TIMER_EXPIRED
    }
}

