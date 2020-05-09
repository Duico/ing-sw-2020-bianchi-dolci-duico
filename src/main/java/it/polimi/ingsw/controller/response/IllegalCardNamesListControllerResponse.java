
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.cli.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.ChallengerCardViewEvent;

public class IllegalCardNamesListControllerResponse extends ControllerResponse {
    public IllegalCardNamesListControllerResponse(ChallengerCardViewEvent message) {
        super(message);
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

