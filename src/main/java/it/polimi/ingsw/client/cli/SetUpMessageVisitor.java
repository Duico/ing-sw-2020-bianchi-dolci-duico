package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.message.DisconnectionMessage;
import it.polimi.ingsw.message.FailedSignUpMessage;
import it.polimi.ingsw.message.ServerLobbyResponse;


public interface SetUpMessageVisitor {
    void visit(FailedSignUpMessage message);
    void visit(ServerLobbyResponse evt);
    void visit(DisconnectionMessage evt);

}
