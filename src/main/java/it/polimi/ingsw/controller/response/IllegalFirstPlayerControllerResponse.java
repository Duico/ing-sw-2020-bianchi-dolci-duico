
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.view.event.FirstPlayerViewEvent;

public class IllegalFirstPlayerControllerResponse extends ControllerResponse {
    private Reason reason;
    public IllegalFirstPlayerControllerResponse(FirstPlayerViewEvent message, Reason reason) {
        super(message);
        this.reason = reason;
    }
    public enum Reason{
        ALREADY_SET,
        NON_EXISTENT
    }

    public Reason getReason() {
        return reason;
    }
}

