
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.ViewEvent;

import java.io.Serializable;

/**
 * defines special kind of response sent from controller to view when an operation is succesfully done
 */
public class SuccessControllerResponse extends ControllerResponse implements Serializable {
    public SuccessControllerResponse(ViewEvent message) {
        super(message);
    }

    @Override
    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

