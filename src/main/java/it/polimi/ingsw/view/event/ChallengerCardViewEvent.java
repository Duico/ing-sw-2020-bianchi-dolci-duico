
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.List;

public class ChallengerCardViewEvent extends GameViewEvent {
    List<String> cardNamesList;
    public ChallengerCardViewEvent(RemoteView view, ArrayList<String> cardNamesList) {
        super(view);
        this.cardNamesList=cardNamesList;
    }

    public List<String> getCardNamesList(){
        return this.cardNamesList;
    }
}

