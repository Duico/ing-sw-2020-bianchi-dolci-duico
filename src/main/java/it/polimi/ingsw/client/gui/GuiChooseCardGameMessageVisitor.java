package it.polimi.ingsw.client.gui;

public class GuiChooseCardGameMessageVisitor extends GuiMessageVisitor {
    public GuiChooseCardGameMessageVisitor(ChooseCardController chooseCardController) {
        super(new GuiChooseCardModelEventVisitor(chooseCardController), new GuiDefaultControllerResponseVisitor(), new GuiDefaultSetUpMessageVisitor(), new GuiDefaultClientConnectionEventVisitor());
    }
}
