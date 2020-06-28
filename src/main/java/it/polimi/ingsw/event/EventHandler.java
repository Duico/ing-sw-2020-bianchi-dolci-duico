package it.polimi.ingsw.event;

import java.util.EventListener;

/**
 * Runnable interface used by EventEmitter to handle different types of events
 */
public interface EventHandler<T extends EventListener> {
    void handleEvent(T listener);
}
