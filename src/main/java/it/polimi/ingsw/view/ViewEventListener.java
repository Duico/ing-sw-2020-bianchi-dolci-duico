package it.polimi.ingsw.view;

import it.polimi.ingsw.view.event.ViewEvent;

import java.util.EventListener;

public interface ViewEventListener extends EventListener {
    void handleEvent(ViewEvent evt);
}
