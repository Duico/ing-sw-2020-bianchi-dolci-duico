package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;

/**
 * class that allows to do different operations using a single method which can handle all
 * different types of SetUpMessage
 */

public interface SetUpMessageVisitor{
    /** Defines behavior in case of a SignUpFailedSetUpMessage
     * @param message SignUpFailedSetUpMessage to be visited
     */
    void visit(SignUpFailedSetUpMessage message);

    /** Defines behavior in case of a InitSetUpMessage
     * @param evt InitSetUpMessage to be visited
     */
    void visit(InitSetUpMessage evt);

    /** Defines behavior in case of a ConnectionMessage
     * @param connectionMessage ConnectionMessage to be visited
     */
    //To inform the cli about the state of the connection
    void visit(ConnectionMessage connectionMessage);


}


