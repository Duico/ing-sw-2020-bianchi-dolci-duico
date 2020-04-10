package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.event.MoveViewEvent;
import it.polimi.ingsw.view.event.ViewEvent;
import it.polimi.ingsw.view.event.ViewEventListener;


public class Controller implements ViewEventListener {

    private Game game; //refer to our model

    public Controller(Game game){
        this.game=game;
    }


    public void handleEvent(ViewEvent message) {
        System.out.println("Generic event from view");
    }

    public void handleEvent(MoveViewEvent message) {

    }


}
