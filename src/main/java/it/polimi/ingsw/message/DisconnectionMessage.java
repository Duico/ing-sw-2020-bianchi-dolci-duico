package it.polimi.ingsw.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

public class DisconnectionMessage extends SetUpMessage {

    public DisconnectionMessage(SetUpType setUpType) {
        super(setUpType);
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {
        visitor.visit(this);
    }

}
