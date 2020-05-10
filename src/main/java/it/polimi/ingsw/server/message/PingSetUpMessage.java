package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;
import it.polimi.ingsw.server.message.SetUpMessage;
import it.polimi.ingsw.server.message.SetUpType;

public class PingSetUpMessage extends SetUpMessage {
    public PingSetUpMessage(SetUpType setUpType) {
        super(setUpType);
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {

    }
}
