package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.event.Message;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.SetUpMessage;
import it.polimi.ingsw.view.ViewEventListener;


public class GuiMessageVisitor implements MessageVisitor {

    protected GuiModelEventVisitor modelEventVisitor;
    protected GuiControllerResponseVisitor controllerResponseVisitor;
    protected GuiSetUpMessageVisitor setUpMessageVisitor;
    protected GuiClientConnectionEventVisitor clientConnectionEventVisitor;
    public GuiMessageVisitor(GuiModel guiModel, SceneEventEmitter sceneEmitter){
        this.modelEventVisitor = new GuiModelEventVisitor(guiModel, sceneEmitter);
        this.controllerResponseVisitor = new GuiControllerResponseVisitor(guiModel);
        this.setUpMessageVisitor = new GuiSetUpMessageVisitor(guiModel);
        this.clientConnectionEventVisitor = new GuiClientConnectionEventVisitor(guiModel, sceneEmitter);
    }


    @Override
    public void visit(ModelEvent evt) {
        evt.accept(modelEventVisitor);
    }

    @Override
    public void visit(ControllerResponse evt) {
        evt.accept(controllerResponseVisitor);
    }

    @Override
    public void visit(SetUpMessage evt) {
        evt.accept(setUpMessageVisitor);
    }

    @Override
    public void visit(ConnectionMessage evt){evt.accept(setUpMessageVisitor);}

    @Override
    public void visit(ClientConnectionEvent evt) { evt.accept(clientConnectionEventVisitor);}

    @Override
    public void addViewEventListener(ViewEventListener listener) {

    }

    @Override
    public void addSignUpListener(SignUpListener listener) {

    }

    @Override
    public void handleEvent(Message evt) {
        evt.accept(this);
    }
}
/*public class GuiMessageVisitor extends MessageVisitor {
    public GuiMessageVisitor(GuiModelEventVisitor guiModelEventVisitor, GuiControllerResponseVisitor guiControllerResponseVisitor, GuiSetUpMessageVisitor guiSetUpMessageVisitor, GuiClientConnectionEventVisitor guiClientConnectionEventVisitor) {
        super(guiModelEventVisitor, guiControllerResponseVisitor, guiSetUpMessageVisitor, guiClientConnectionEventVisitor);
    }
}*/
