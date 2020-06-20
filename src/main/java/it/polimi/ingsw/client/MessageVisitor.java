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

/*
public abstract class MessageVisitor implements MessageListener {
    protected ModelEventVisitor modelEventVisitor;
    protected ControllerResponseVisitor controllerResponseVisitor;
    protected SetUpMessageVisitor setUpMessageVisitor;
    protected ClientConnectionEventVisitor clientConnectionEventVisitor;
    public MessageVisitor(ModelEventVisitor modelEventVisitor, ControllerResponseVisitor controllerResponseVisitor, SetUpMessageVisitor setUpMessageVisitor, ClientConnectionEventVisitor clientConnectionEventVisitor){
        this.modelEventVisitor = modelEventVisitor;
        this.controllerResponseVisitor = controllerResponseVisitor;
        this.setUpMessageVisitor = setUpMessageVisitor;
        this.clientConnectionEventVisitor = clientConnectionEventVisitor;
    }


    public void visit(ModelEvent evt) {
        evt.accept(modelEventVisitor);
    }

    public void visit(ControllerResponse evt) {
        evt.accept(controllerResponseVisitor);
    }

    public void visit(SetUpMessage evt) {
        evt.accept(setUpMessageVisitor);
    }

    public void visit(ConnectionMessage evt){evt.accept(setUpMessageVisitor);}

    public void visit(ClientConnectionEvent evt) { evt.accept(clientConnectionEventVisitor);}

    public void addViewEventListener(ViewEventListener listener){
        setUpMessageVisitor.addEventListener(ViewEventListener.class, listener);
        controllerResponseVisitor.addEventListener(ViewEventListener.class, listener);
        modelEventVisitor.addEventListener(ViewEventListener.class, listener);
    }

    public void addSignUpListener(SignUpListener listener){
        setUpMessageVisitor.addEventListener(SignUpListener.class, listener);
        controllerResponseVisitor.addEventListener(SignUpListener.class, listener);
        modelEventVisitor.addEventListener(SignUpListener.class, listener);
    }
    @Override
    public void handleEvent(Message evt) {
        evt.accept(this);
    }
}*/
