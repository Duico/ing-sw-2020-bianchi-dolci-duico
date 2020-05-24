package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ModelEventVisitor;

public abstract class GuiModelEventVisitor extends ModelEventVisitor {
    SceneEventEmitter sceneEventEmitter;
    protected void setSceneEventEmitter(SceneEventEmitter sceneEventEmitter) {
        this.sceneEventEmitter = sceneEventEmitter;
    }

}
