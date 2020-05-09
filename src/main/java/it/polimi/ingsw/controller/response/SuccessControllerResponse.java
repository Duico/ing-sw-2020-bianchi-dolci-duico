
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.cli.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.ChallengerCardViewEvent;
import it.polimi.ingsw.view.event.GameViewEvent;
import it.polimi.ingsw.view.event.ViewEvent;

import java.io.Serializable;

public class SuccessControllerResponse extends ControllerResponse implements Serializable {
    public SuccessControllerResponse(ViewEvent message) {
        super(message);
    }

    @Override
    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

