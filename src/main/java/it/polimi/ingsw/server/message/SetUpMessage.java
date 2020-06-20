package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.SetUpMessageVisitor;

public abstract class SetUpMessage extends GameMessage {

    private SetUpType setUpType;

    public SetUpMessage(SetUpType setUpType){
        this.setUpType=setUpType;
    }

    public SetUpType getSetUpType() {
        return setUpType;
    }

    public void accept(MessageVisitor visitor){
        visitor.visit(this);
    }

    public abstract void accept(SetUpMessageVisitor visitor);

}
