package it.polimi.ingsw.message;

import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

import java.util.ArrayList;

public class PingMessage extends SetUpMessage {
    private SetUpType setUpType;
    public PingMessage(SetUpType setUpType) {
        this.setUpType=setUpType;
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {

    }
}
