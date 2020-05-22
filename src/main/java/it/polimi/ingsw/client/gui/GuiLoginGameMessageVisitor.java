package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.GameMessageVisitor;

public class GuiLoginGameMessageVisitor extends GameMessageVisitor {
    public GuiLoginGameMessageVisitor(GuiModelEventVisitor guiModelEventVisitor, GuiControllerResponseVisitor guiControllerResponseVisitor, GuiLoginSetUpMessageVisitor guiSetUpMessageVisitor){
        //Cli property could be replaced by an event listenerguiSetUpMessageVisitor
        super(guiModelEventVisitor, guiControllerResponseVisitor, guiSetUpMessageVisitor);
    }

}
