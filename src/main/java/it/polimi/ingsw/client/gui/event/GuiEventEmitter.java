package it.polimi.ingsw.client.gui.event;

import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.model.Position;

import java.util.List;

public interface GuiEventEmitter {
    void setEventListener(GuiEventListener listener);
}
