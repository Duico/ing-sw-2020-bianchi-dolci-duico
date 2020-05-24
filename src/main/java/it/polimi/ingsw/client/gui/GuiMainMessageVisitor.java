package it.polimi.ingsw.client.gui;

public class GuiMainMessageVisitor extends GuiMessageVisitor {
    public GuiMainMessageVisitor(MainController mainController){
        super(new GuiMainModelEventVisitor(mainController), new GuiDefaultControllerResponseVisitor(), new GuiDefaultSetUpMessageVisitor(), new GuiDefaultClientConnectionEventVisitor());
    }

}
