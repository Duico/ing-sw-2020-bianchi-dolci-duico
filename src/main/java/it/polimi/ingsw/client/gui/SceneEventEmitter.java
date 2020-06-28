package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.event.EventEmitter;

/**
 * class used to emit scene events for gui
 */
public class SceneEventEmitter extends EventEmitter{
    public void emitEvent(SceneEvent sceneEvent){
        executeEventListeners(SceneEventListener.class, (listener) -> {
            listener.handleEvent(sceneEvent);
        });
    }
}
