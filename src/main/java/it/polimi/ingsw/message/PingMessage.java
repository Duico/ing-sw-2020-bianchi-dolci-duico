package it.polimi.ingsw.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

public class PingMessage extends SetUpMessage {
    public PingMessage(SetUpType setUpType) {
        super(setUpType);
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {

    }
}
