package it.polimi.ingsw.view;

import it.polimi.ingsw.view.event.ViewEvent;

import java.util.EventListener;

/**
 * listener interface for view event messages
 */
public interface ViewEventListener extends EventListener {
    void handleEvent(ViewEvent evt);
}
