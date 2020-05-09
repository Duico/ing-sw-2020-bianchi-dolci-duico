
package it.polimi.ingsw.controller.response;
import it.polimi.ingsw.client.cli.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.ViewEvent;

import java.io.Serializable;

public abstract class ControllerResponse implements Serializable {
    private ViewEvent event;
    public ControllerResponse(ViewEvent event){
        this.event = event;
    }

    public ViewEvent getEvent() {
        return event;
    }
    public abstract void accept(ControllerResponseVisitor visitor);
}

