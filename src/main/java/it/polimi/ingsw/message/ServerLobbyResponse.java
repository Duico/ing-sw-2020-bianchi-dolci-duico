package it.polimi.ingsw.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

public class ServerLobbyResponse extends SetUpMessage {

    private SingUpParameter response;

    public ServerLobbyResponse(SingUpParameter response){
        this.response=response;
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {
        visitor.visit(this);
    }

    public enum SingUpParameter {
        STARTGAME,
        NICKNAME,
        CORRECT_SIGNUP
    }

    public SingUpParameter getResponse() {
        return response;
    }
}
