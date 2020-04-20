package it.polimi.ingsw.event;

import java.util.EventListener;

public interface EventHandler<T extends EventListener> {
    void handleEvent(T listener);
}
