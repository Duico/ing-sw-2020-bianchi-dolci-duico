
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.view.event.ViewEvent;

/**
 * defines special kind of response sent from controller to view when a certain kind of operation
 * is not allowed in current turn phase
 */
public class IllegalTurnPhaseControllerResponse extends ControllerResponse {
    private TurnPhase requiredPhase;
    public IllegalTurnPhaseControllerResponse(ViewEvent event, TurnPhase requiredPhase) {
        super(event);
        this.requiredPhase = requiredPhase;
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

