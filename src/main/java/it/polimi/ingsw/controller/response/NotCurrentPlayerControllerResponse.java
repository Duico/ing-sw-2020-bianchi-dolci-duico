
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.ViewEvent;

/**
 * defines special kind of response sent from controller to view when a player which is not allowed to do operations
 * at the moment tries to do one
 */
public class NotCurrentPlayerControllerResponse extends ControllerResponse {
    public NotCurrentPlayerControllerResponse(ViewEvent message) {
        super(message);
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}
