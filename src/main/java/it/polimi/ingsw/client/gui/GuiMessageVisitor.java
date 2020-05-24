package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.MessageVisitor;

public abstract class GuiMessageVisitor extends MessageVisitor {

    public GuiMessageVisitor(GuiModelEventVisitor modelEventVisitor, GuiControllerResponseVisitor controllerResponseVisitor, GuiSetUpMessageVisitor setUpMessageVisitor, GuiClientConnectionEventVisitor clientConnectionEventVisitor) {
        super(modelEventVisitor, controllerResponseVisitor, setUpMessageVisitor, clientConnectionEventVisitor);
    }
}
