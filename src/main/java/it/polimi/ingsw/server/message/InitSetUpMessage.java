package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.SetUpMessageVisitor;
import it.polimi.ingsw.model.Player;

/**
 * Represent message sent from server to client when connection has already been established
 * and lobby has already been created
 */
public class InitSetUpMessage extends SetUpMessage {

    private SignUpParameter response;
    private Player player;
    private boolean askPersistency;

    public InitSetUpMessage(SetUpType setUpType, SignUpParameter response){
        this(setUpType, response, null);
    }
    public InitSetUpMessage(SetUpType setUpType, SignUpParameter response, boolean askPersistency){
        this(setUpType, response, null, askPersistency);
    }
    public InitSetUpMessage(SetUpType setUpType, SignUpParameter response, Player newPlayer, boolean askPersistency){
        super(setUpType);
        this.response=response;
        this.player = newPlayer;
        this.askPersistency = askPersistency;
    }
    public InitSetUpMessage(SetUpType setUpType, SignUpParameter response, Player newPlayer){
        this(setUpType, response, newPlayer, false);
    }

    @Override
    public void accept(SetUpMessageVisitor visitor) {
        visitor.visit(this);
    }

    public enum SignUpParameter {
        STARTGAME,
        NICKNAME,
        CORRECT_SIGNUP_WAIT,
        CORRECT_SIGNUP_STARTING
    }

    public SignUpParameter getResponse() {
        return response;
    }

    public Player getPlayer(){
        return player;
    }

    public boolean isAskPersistency() {
        return askPersistency;
    }
}
