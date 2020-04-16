package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;

public class FirstPlayerViewEvent extends ViewEvent {
    private String nickName;
    public FirstPlayerViewEvent(RemoteView view, String nickName) {
        super(view);
        this.nickName=nickName;
    }

    public String getNickName(){
        return this.nickName;
    }
}
