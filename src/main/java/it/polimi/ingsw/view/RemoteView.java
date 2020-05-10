package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.server.ViewConnection;
import it.polimi.ingsw.view.event.*;

import java.io.Serializable;
import java.util.ArrayList;

public class RemoteView extends View implements ViewEventListener, ModelEventListener {

    private ViewConnection viewConnection;


    public RemoteView(Player player, ViewConnection c) {
        super(player);
        this.viewConnection = c;
        viewConnection.addObserver(this);
    }

    /*public String getNickName(){
        return player.getNickName();
    }*/

    /*public Player getPlayer(){
        return player;
    }*/

    public void sendMessage(Object message){
        viewConnection.asyncSend(message);
    }

    @Override
    public void sendMessage(ModelEvent modelEvent){
        viewConnection.asyncSend(modelEvent);
        //sendMessage(modelEvent);
    }

    @Override
    public void handleEvent(ViewEvent evt){
        evt.setView(this);
        evt.accept(this);
    }

    public void eventResponse(ControllerResponse response){
        //we don't want to send the remote view to client
        response.getEvent().setView(null);
        viewConnection.asyncSend(response);
    }




}

