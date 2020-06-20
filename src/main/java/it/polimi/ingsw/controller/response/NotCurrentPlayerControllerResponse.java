
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.ViewEvent;

public class NotCurrentPlayerControllerResponse extends ControllerResponse {
    public NotCurrentPlayerControllerResponse(ViewEvent message) {
        super(message);
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}
