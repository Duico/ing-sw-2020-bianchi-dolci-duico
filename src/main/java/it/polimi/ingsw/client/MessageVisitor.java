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

/**
 * class that allows to do different operations using a single method which can handle all
 * different types of messages
 */
public interface MessageVisitor extends MessageListener {

    /** Defines behavior in case of a ModelEvent
     * @param evt ModelEvent to be visited
     */
    void visit(ModelEvent evt);

    /** Defines behavior in case of a ControllerResponse
     * @param evt ControllerResponse to be visited
     */
    void visit(ControllerResponse evt);

    /** Defines behavior in case of a SetUpMessage
     * @param evt SetUpMessage to be visited
     */
    void visit(SetUpMessage evt);

    /** Defines behavior in case of a ConnectionMessage
     * @param evt ConnectionMessage to be visited
     */
    void visit(ConnectionMessage evt);

    /** Defines behavior in case of a ClientConnectionEvent
     * @param evt ClientConnectionEvent to be visited
     */
    void visit(ClientConnectionEvent evt);

    /** add a viewEvent listener
     * @param listener a ViewEvent listener
     */
    void addViewEventListener(ViewEventListener listener);

    /** add a signUp listener
     * @param listener a SignUpMessage listener
     */
    void addSignUpListener(SignUpListener listener);
    @Override
    void handleEvent(Message evt);
}

