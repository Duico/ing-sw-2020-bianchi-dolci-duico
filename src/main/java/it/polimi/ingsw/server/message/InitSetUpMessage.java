package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

public class InitSetUpMessage extends SetUpMessage {

    private SignUpParameter response;

    public InitSetUpMessage(SetUpType setUpType, SignUpParameter response){
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
