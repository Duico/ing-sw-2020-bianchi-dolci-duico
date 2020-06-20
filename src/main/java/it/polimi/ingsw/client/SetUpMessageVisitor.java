package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;


public interface SetUpMessageVisitor{
    void visit(SignUpFailedSetUpMessage message);
    void visit(InitSetUpMessage evt);
    //To inform the cli about the state of the connection
    void visit(ConnectionMessage connectionMessage);


}


