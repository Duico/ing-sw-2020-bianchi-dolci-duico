
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.FirstPlayerViewEvent;


/**
 * defines special kind of response sent from controller to view when an illegal first player has been chosen
 * with a related reason
 */
public class IllegalFirstPlayerControllerResponse extends ControllerResponse {
    private Reason reason;
    public IllegalFirstPlayerControllerResponse(FirstPlayerViewEvent message, Reason reason) {
        super(message);
        this.reason = reason;
    }

    /**
     * defines particular reased that explains why first player chosen is illegal
     */
    public enum Reason{
        ALREADY_SET,
        NON_EXISTENT
    }

    public Reason getReason() {
        return reason;
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

