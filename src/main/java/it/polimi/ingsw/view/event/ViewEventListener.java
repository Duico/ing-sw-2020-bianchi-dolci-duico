package it.polimi.ingsw.view.event;

import java.util.EventListener;

public interface ViewEventListener extends EventListener {
    void move(MoveViewEvent messsage);

}
