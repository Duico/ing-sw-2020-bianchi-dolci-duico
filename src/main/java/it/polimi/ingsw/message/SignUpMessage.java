package it.polimi.ingsw.message;
import it.polimi.ingsw.client.cli.SetUpMessageVisitor;

import java.io.Serializable;
import java.util.ArrayList;

public class SignUpMessage implements Serializable {

    private String nickName;
    private Integer numPlayers;

    public SignUpMessage(String nickName, Integer numPlayers) {
        this.nickName=nickName;
        this.numPlayers=numPlayers;
    }

    public SignUpMessage(String nickName){
        this(nickName, null);
    }

    public String getNickName() {
        return nickName;
    }

    public int getNumPlayers() {
        return numPlayers;
    }


}
