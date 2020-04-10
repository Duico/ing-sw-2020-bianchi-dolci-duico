package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Turn;
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
        Player player = game.getCurrentPlayer();

        int worker = message.getWorkerId();
        if(message.getPlayer() != player) return; //todo an event Wrong player
        else {
            Turn turn = game.getTurn();
            if(!turn.updateCurrentWorker(worker)){
                return; //todo an event Wrong worker
            }
            else{
                if(turn.isRequiredToMove()==false && turn.isAllowedToMove()== false){
                    return; //todo an event not allowed movement
                }
                else if(turn.move(message.getDestPosition())) return; //todo an event not allowed movement
                    else return; //todo an event not allowed movement


                }
            }

        }

}
