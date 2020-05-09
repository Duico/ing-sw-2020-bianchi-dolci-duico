package it.polimi.ingsw.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class SetUpMessage implements Serializable {

    public SetUpMessage(){ }

    public abstract void accept(SetUpMessageVisitor visitor);

}
