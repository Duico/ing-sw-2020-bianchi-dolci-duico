package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.*;
import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.event.Message;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.SetUpMessage;
import it.polimi.ingsw.view.ViewEventListener;

/**
 * Class which contains the visitor for model, controllerResponse, setUpMessage and clientConnection event
 */
public class CliMessageVisitor implements MessageVisitor {

    protected CliModelEventVisitor modelEventVisitor;
    protected CliControllerResponseVisitor controllerResponseVisitor;
    protected CliSetUpMessageVisitor setUpMessageVisitor;
    protected CliClientConnectionEventVisitor clientConnectionEventVisitor;

    public CliMessageVisitor(Cli cli, CliModel cliModel){

        this.modelEventVisitor = new CliModelEventVisitor(cli, cliModel);
        this.controllerResponseVisitor = new CliControllerResponseVisitor(cli, cliModel);
        this.setUpMessageVisitor = new CliSetUpMessageVisitor(cli, cliModel);
        this.clientConnectionEventVisitor = new CliClientConnectionEventVisitor(cli, cliModel);

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
    public void visit(ConnectionMessage evt) {
        evt.accept(setUpMessageVisitor);
    }

    @Override
    public void visit(ClientConnectionEvent evt) {
        evt.accept(clientConnectionEventVisitor);
    }

    /**
     * Funtcion which add to the visitors a ViewEventListener
     * @param listener
     */
    @Override
    public void addViewEventListener(ViewEventListener listener){
        setUpMessageVisitor.addEventListener(ViewEventListener.class, listener);
        controllerResponseVisitor.addEventListener(ViewEventListener.class, listener);
        modelEventVisitor.addEventListener(ViewEventListener.class, listener);
    }

    /**
     * Funtcion which add to the visitors a SignUpListener
     * @param listener
     */
    public void addSignUpListener(SignUpListener listener){
        setUpMessageVisitor.addEventListener(SignUpListener.class, listener);
        controllerResponseVisitor.addEventListener(SignUpListener.class, listener);
        modelEventVisitor.addEventListener(SignUpListener.class, listener);
    }

    @Override
    public void handleEvent(Message evt) {

    }

}
