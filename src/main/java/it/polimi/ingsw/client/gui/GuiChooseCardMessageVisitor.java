package it.polimi.ingsw.client.gui;

public class GuiChooseCardMessageVisitor extends GuiMessageVisitor {
    public GuiChooseCardMessageVisitor(ChooseCardController chooseCardController) {
        super(new GuiChooseCardModelEventVisitor(chooseCardController), new GuiDefaultControllerResponseVisitor(), new GuiDefaultSetUpMessageVisitor(), new GuiDefaultClientConnectionEventVisitor());
    }
}
