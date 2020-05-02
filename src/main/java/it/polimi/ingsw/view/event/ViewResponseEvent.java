
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.event.Event;

public class ViewResponseEvent extends Event {
    private ViewEvent originalEvent;
    public ViewResponseEvent(ViewEvent evt){
        this.originalEvent = evt;
    }
    public ViewEvent getOriginalEvent(){
        return this.originalEvent;
    }

}

