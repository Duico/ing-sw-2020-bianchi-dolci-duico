package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.ControllerResponseVisitor;
import it.polimi.ingsw.client.cli.ModelEventVisitor;
import it.polimi.ingsw.client.cli.SetUpMessageVisitor;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.SetUpMessage;
import it.polimi.ingsw.view.ViewEventListener;

import java.util.EventListener;

public abstract class GameMessageVisitor {
    protected ModelEventVisitor modelEventVisitor;
    protected ControllerResponseVisitor controllerResponseVisitor;
    protected SetUpMessageVisitor setUpMessageVisitor;
    public GameMessageVisitor(ModelEventVisitor modelEventVisitor, ControllerResponseVisitor controllerResponseVisitor, SetUpMessageVisitor setUpMessageVisitor){
        this.modelEventVisitor = modelEventVisitor;
        this.controllerResponseVisitor = controllerResponseVisitor;
        this.setUpMessageVisitor = setUpMessageVisitor;
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

    public void visit(ConnectionMessage evt) { evt.accept(setUpMessageVisitor);}


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
}
