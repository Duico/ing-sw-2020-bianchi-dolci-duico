
package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.CardViewEvent;

import java.util.List;

public class IllegalCardNameControllerResponse extends ControllerResponse {
    private List<String> expectedCardNames;
    public IllegalCardNameControllerResponse(CardViewEvent message, List<String> expectedCardNames) {
        super(message);
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}

