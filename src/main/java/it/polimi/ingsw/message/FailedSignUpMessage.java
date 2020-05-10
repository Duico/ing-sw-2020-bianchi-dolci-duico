package it.polimi.ingsw.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

public class FailedSignUpMessage extends SetUpMessage {

    private Reason reason;

    public FailedSignUpMessage(SetUpType setUpType, Reason reason){
        super(setUpType);
        this.reason=reason;
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {
        visitor.visit(this);
    }

    public enum Reason{
        NICKNAME_ALREADY_USED,
        INVALID_NICKNAME,
        INVALID_NUMPLAYERS,
    }

    public Reason getReason() {
        return reason;
    }
}
