package it.polimi.ingsw.controller.response;

import it.polimi.ingsw.view.event.ViewEvent;

public class ControllerResponse{
    private ViewEvent event;
    public ControllerResponse(ViewEvent event){
        this.event = event;
    }

    public ViewEvent getEvent() {
        return event;
    }
}
