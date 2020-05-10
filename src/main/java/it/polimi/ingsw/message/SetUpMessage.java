package it.polimi.ingsw.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

import java.io.Serializable;
import java.util.ArrayList;

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
