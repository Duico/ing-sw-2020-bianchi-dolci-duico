package it.polimi.ingsw.client.gui;

public class GuiLoginGameMessageVisitor extends GuiMessageVisitor {
    public GuiLoginGameMessageVisitor(LoginController loginController){
        //Cli property could be replaced by an event listenerguiSetUpMessageVisitor
        super(new GuiDefaultModelEventVisitor(), new GuiDefaultControllerResponseVisitor(), new GuiLoginSetUpMessageVisitor(loginController), new GuiDefaultClientConnectionEventVisitor());
    }

}
