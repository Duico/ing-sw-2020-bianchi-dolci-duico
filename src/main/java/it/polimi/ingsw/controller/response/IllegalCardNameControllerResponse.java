
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.CardViewEvent;

import java.util.List;

/**
 * defines special kind of response sent from controller to view when illegal card has been chosen
 */
public class IllegalCardNameControllerResponse extends ControllerResponse {
    private List<String> expectedCardNames;
    public IllegalCardNameControllerResponse(CardViewEvent message, List<String> expectedCardNames) {
        super(message);
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

