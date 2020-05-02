
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.view.event.ViewEvent;

public class IllegalTurnPhaseControllerResponse extends ControllerResponse {
    private TurnPhase requiredPhase;
    public IllegalTurnPhaseControllerResponse(ViewEvent event, TurnPhase requiredPhase) {
        super(event);
        this.requiredPhase = requiredPhase;
    }
}

