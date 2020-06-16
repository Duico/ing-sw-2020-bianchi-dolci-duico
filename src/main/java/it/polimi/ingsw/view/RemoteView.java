package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.server.ViewConnection;
import it.polimi.ingsw.view.event.*;

public class RemoteView extends View implements ViewEventListener, ModelEventListener, ControllerResponseListener {

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

    public void sendMessage(String message){
        //sendMessage(message)
    }
    public void sendMessage(Object message){
        viewConnection.asyncSend(message);
    }

    @Override
    public void sendMessage(ModelEvent modelEvent){
        viewConnection.asyncSend(modelEvent);
        //sendMessage(modelEvent);
    }

    @Override
    public void removeDefeatedPlayer(Player player) {
        if(getPlayer().equalsUuid(player))
            viewConnection.removeDefeatedPlayer();
    }


    @Override
    public void handleEvent(ViewEvent evt){
        evt.setPlayer(getPlayer());
        evt.accept(this);

    }

    @Override
    public void eventResponse(ControllerResponse response){
        //we don't want to send the remote view to client
        System.out.println(getPlayer());
        System.out.println(response.getEvent().getPlayer());
        if(response.getEvent().getPlayer().equalsUuid(getPlayer())) {
            //response.getEvent().setPlayer(null);
            viewConnection.asyncSend(response);
        }
    }




}

