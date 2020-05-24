package it.polimi.ingsw.client.gui;

public class GuiMainGameMessageVisitor extends GuiMessageVisitor {
    public GuiMainGameMessageVisitor(MainController mainController){
        super(new GuiMainModelEventVisitor(mainController), new GuiDefaultControllerResponseVisitor(), new GuiDefaultSetUpMessageVisitor(), new GuiDefaultClientConnectionEventVisitor());
    }

}
