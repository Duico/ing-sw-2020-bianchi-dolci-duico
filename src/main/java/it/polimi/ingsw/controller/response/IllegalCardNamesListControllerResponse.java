
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.ChallengerCardViewEvent;

/**
 * defines special kind of response sent from controller to view when illegal list of cards has been chosen
 */
public class IllegalCardNamesListControllerResponse extends ControllerResponse {
    public IllegalCardNamesListControllerResponse(ChallengerCardViewEvent message) {
        super(message);
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

