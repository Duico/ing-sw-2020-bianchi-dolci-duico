package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

public class SignUpFailedSetUpMessage extends SetUpMessage {

    private Reason reason;

    public SignUpFailedSetUpMessage(SetUpType setUpType, Reason reason){
        super(setUpType);
        this.reason=reason;
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {
        visitor.visit(this);
    }

    public enum Reason{
        NULL_LOBBY,
        INVALID_NICKNAME,
        INVALID_NUMPLAYERS,
        GAME_ALREADY_START
    }

    public Reason getReason() {
        return reason;
    }
}
