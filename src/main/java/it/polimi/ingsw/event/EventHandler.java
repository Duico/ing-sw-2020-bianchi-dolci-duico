package it.polimi.ingsw.event;

import java.util.EventListener;

/**
 * interface used to handle different types of events
 */
public interface EventHandler<T extends EventListener> {
    void handleEvent(T listener);
}
