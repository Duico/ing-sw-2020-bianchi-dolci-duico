package it.polimi.ingsw.client.gui;

import java.util.EventListener;

public interface SceneEventListener extends EventListener {
    void handleEvent(SceneEvent evt);
}
