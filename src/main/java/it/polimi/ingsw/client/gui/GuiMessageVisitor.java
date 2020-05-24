package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.MessageVisitor;

public abstract class GuiMessageVisitor extends MessageVisitor {
    protected SceneEventEmitter sceneEventEmitter = new SceneEventEmitter();
    public GuiMessageVisitor(GuiModelEventVisitor modelEventVisitor, GuiControllerResponseVisitor controllerResponseVisitor, GuiSetUpMessageVisitor setUpMessageVisitor, GuiClientConnectionEventVisitor clientConnectionEventVisitor) {
        super(modelEventVisitor, controllerResponseVisitor, setUpMessageVisitor, clientConnectionEventVisitor);
        modelEventVisitor.setSceneEventEmitter(sceneEventEmitter);
    }

    public void addSceneEventListener(SceneEventListener listener){
        sceneEventEmitter.addEventListener(SceneEventListener.class, listener);
    }
}
