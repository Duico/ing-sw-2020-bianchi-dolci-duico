package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;


public interface SetUpMessageVisitor {
    void visit(SignUpFailedSetUpMessage message);
    void visit(InitSetUpMessage evt);
//    void visit(DisconnectionSetUpMessage evt);

}
