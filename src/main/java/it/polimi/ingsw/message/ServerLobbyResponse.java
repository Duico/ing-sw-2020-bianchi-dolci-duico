package it.polimi.ingsw.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

public class ServerLobbyResponse extends SetUpMessage {

    private SignUpParameter response;

    public ServerLobbyResponse(SetUpType setUpType, SignUpParameter response){
        super(setUpType);
        this.response=response;
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {
        visitor.visit(this);
    }

    public enum SignUpParameter {
        STARTGAME,
        NICKNAME,
        CORRECT_SIGNUP
    }

    public SignUpParameter getResponse() {
        return response;
    }
}
