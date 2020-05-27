package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnectionEventVisitor;
import it.polimi.ingsw.client.event.ClientConnectionEvent;

public class GuiClientConnectionEventVisitor extends ClientConnectionEventVisitor {
    protected final GuiModel guiModel;
    protected GuiClientConnectionEventVisitor(GuiModel guiModel) {
        this.guiModel = guiModel;
    }

    @Override
    public void visit(ClientConnectionEvent evt) {

    }
}
