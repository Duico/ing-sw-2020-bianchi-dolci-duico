
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.List;

public class ChallengerCardViewEvent extends GameViewEvent {
    List<String> cardNamesList = new ArrayList<>();
    public ChallengerCardViewEvent(RemoteView view) {
        super(view);
    }

    public List<String> getCardNamesList(){
        return this.cardNamesList;
    }
}

