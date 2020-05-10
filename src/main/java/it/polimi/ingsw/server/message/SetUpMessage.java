package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

import java.io.Serializable;

public abstract class SetUpMessage implements Serializable {

    private SetUpType setUpType;

    public SetUpMessage(SetUpType setUpType){
        this.setUpType=setUpType;
    }

    public SetUpType getSetUpType() {
        return setUpType;
    }

    public abstract void accept(SetUpMessageVisitor visitor);

}
