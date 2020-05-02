package it.polimi.ingsw.view;

import java.util.EventListener;

public interface ViewEventListener extends EventListener {
    void handleEvent(Object e);
}
