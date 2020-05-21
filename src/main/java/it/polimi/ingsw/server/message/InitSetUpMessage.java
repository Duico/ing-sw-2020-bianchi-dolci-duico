package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.SetUpMessageVisitor;
import it.polimi.ingsw.model.Player;

public class InitSetUpMessage extends SetUpMessage {

    private SignUpParameter response;
    private Player player;

    public InitSetUpMessage(SetUpType setUpType, SignUpParameter response){
        this(setUpType, response, null);
    }
    public InitSetUpMessage(SetUpType setUpType, SignUpParameter response, Player newPlayer){
        super(setUpType);
        this.response=response;
        this.player = newPlayer;
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {
        visitor.visit(this);
    }

    public enum SignUpParameter {
        STARTGAME,
        NICKNAME,
        CORRECT_SIGNUP_WAIT,
        CORRECT_SIGNUP_LAST
    }

    public SignUpParameter getResponse() {
        return response;
    }

    public Player getPlayer(){
        return player;
    };
}
