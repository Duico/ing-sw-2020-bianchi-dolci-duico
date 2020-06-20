package it.polimi.ingsw.client;

import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.client.event.MessageListener;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.event.Message;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.GameMessage;
import it.polimi.ingsw.server.message.SetUpMessage;
import it.polimi.ingsw.view.ViewEventListener;


public interface MessageVisitor extends MessageListener {

    void visit(ModelEvent evt);
    void visit(ControllerResponse evt);
    void visit(SetUpMessage evt);
    void visit(ConnectionMessage evt);
    void visit(ClientConnectionEvent evt);
    void addViewEventListener(ViewEventListener listener);
    void addSignUpListener(SignUpListener listener);
    @Override
    void handleEvent(Message evt);
}

