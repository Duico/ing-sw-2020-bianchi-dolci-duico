package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;

public class GuiDefaultSetUpMessageVisitor extends GuiSetUpMessageVisitor {

    @Override
    public void visit(SignUpFailedSetUpMessage message) {

    }

    @Override
    public void visit(InitSetUpMessage evt) {

    }

    @Override
    public void visit(ConnectionMessage connectionMessage) {

    }
}
