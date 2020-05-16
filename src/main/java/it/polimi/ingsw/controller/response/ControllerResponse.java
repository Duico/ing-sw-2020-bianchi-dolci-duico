
package it.polimi.ingsw.controller.response;
import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.client.cli.ControllerResponseVisitor;
import it.polimi.ingsw.server.message.GameMessage;
import it.polimi.ingsw.view.event.ViewEvent;

public abstract class ControllerResponse extends GameMessage {
    private ViewEvent event;
    public ControllerResponse(ViewEvent event){
        this.event = event;
    }

    public ViewEvent getEvent() {
        return event;
    }

    public void accept(GameMessageVisitor visitor){
        visitor.visit(this);
    }

    public abstract void accept(ControllerResponseVisitor visitor);
}

