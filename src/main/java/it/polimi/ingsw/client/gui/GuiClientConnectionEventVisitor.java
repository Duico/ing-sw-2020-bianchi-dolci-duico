package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnectionEventVisitor;
import it.polimi.ingsw.client.event.ClientConnectionEvent;

public class GuiClientConnectionEventVisitor extends ClientConnectionEventVisitor {
    private final GuiModel guiModel;
    private final SceneEventEmitter sceneEventEmitter;
    protected GuiClientConnectionEventVisitor(GuiModel guiModel, SceneEventEmitter sceneEventEmitter) {
        this.guiModel = guiModel;
        this.sceneEventEmitter = sceneEventEmitter;
    }

    @Override
    public void visit(ClientConnectionEvent evt) {
    }
}
