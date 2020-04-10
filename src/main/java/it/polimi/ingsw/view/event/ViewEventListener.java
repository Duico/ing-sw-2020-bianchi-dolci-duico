package it.polimi.ingsw.view.event;

import java.util.EventListener;

public interface ViewEventListener extends EventListener {
    void handleEvent(ViewEvent e);
    void handleEvent(MoveViewEvent e);

}
