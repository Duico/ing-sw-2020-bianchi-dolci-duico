package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.ModelEventVisitor;

public class GuiMessageVisitor extends MessageVisitor {
    public GuiMessageVisitor(GuiModelEventVisitor guiModelEventVisitor, GuiControllerResponseVisitor guiControllerResponseVisitor, GuiSetUpMessageVisitor guiSetUpMessageVisitor, GuiClientConnectionEventVisitor guiClientConnectionEventVisitor) {
        super(guiModelEventVisitor, guiControllerResponseVisitor, guiSetUpMessageVisitor, guiClientConnectionEventVisitor);
    }
}
