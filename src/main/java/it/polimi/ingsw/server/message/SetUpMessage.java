package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

import java.io.Serializable;

public abstract class SetUpMessage extends GameMessage {

    private SetUpType setUpType;

    public SetUpMessage(SetUpType setUpType){
        this.setUpType=setUpType;
    }

    public SetUpType getSetUpType() {
        return setUpType;
    }

    public void accept(GameMessageVisitor visitor){
        visitor.visit(this);
    }

    public abstract void accept(SetUpMessageVisitor visitor);

}
