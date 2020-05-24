package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;


public abstract class SetUpMessageVisitor extends ClientEventEmitter {
    public abstract void visit(SignUpFailedSetUpMessage message);
    public abstract void visit(InitSetUpMessage evt);
    //To inform the cli about the state of the connection
    public abstract void visit(ConnectionMessage connectionMessage);
//    void visit(DisconnectionSetUpMessage evt);
}
