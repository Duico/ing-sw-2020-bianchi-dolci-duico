package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.SetUpMessageVisitor;

public class PingSetUpMessage extends SetUpMessage {
    public PingSetUpMessage(SetUpType setUpType) {
        super(setUpType);
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {

    }
}
