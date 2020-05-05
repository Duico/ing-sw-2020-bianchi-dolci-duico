package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.server.ViewConnection;
import it.polimi.ingsw.view.event.*;
import java.util.ArrayList;

public class RemoteView extends ViewEventVisitor implements ViewEventListener, ModelEventListener {

    private ViewConnection viewConnection;
    private Player player;

    public RemoteView(Player player, ViewConnection c) {
        this.player=player;
        this.viewConnection = c;
        viewConnection.addObserver(this);
    }

    public String getNickName(){
        return player.getNickName();
    }

    public Player getPlayer(){
        return player;
    }

    public void sendMessage(Object message){
        viewConnection.asyncSend(message);
    }

    @Override
    public void sendMessage(ModelEvent modelEvent){
        sendMessage(modelEvent);
    }

    @Override
    public void handleEvent(ViewEvent evt){
        evt.setView(this);
        evt.accept(this);
    }

    public void eventResponse(ControllerResponse response){
        //send to Client View
    }

//    //Basta una funzione unica che accetti un ModelEvent, non servono tante funzioni specifiche
//    //
//    //TODO trasformare tutto in ModelEvent, senza Message
//    @Override
//    public void movement(MoveWorkerModelEvent e) {
//        System.out.println("Remote View Movement");
//        /*OperationMessage message = new OperationMessage(Operation.MOVE, e.getStartPosition(), e.getDestinationPosition() , false);
//        sendMessage(message);*/
//    }
//    //TODO trasformare tutto in ModelEvent, senza Message
//    @Override
//    public void build(BuildWorkerModelEvent e) {
//        /*OperationMessage message = new OperationMessage(Operation.BUILD, e.getStartPosition(), e.getDestinationPosition() , e.isDome());
//        sendMessage(message);*/
//
//    }
//
//
//    //TODO trasformare tutto in ModelEvent, senza Message
//    @Override
//    public void place(PlaceWorkerModelEvent e) {
//        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
//            OperationMessage message = new OperationMessage(Operation.PLACE, e.getPlacePosition(), null , false);
//            sendMessage(message);
//        }
//
//
//    }

//
//
//    @Override
//    public void newTurn(NewTurnModelEvent e) {
//        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
//            SetUpMessage message = new SetUpMessage(SetUpType.NEWTURN, null);
//            sendMessage(message);
//        }else
//            sendMessage("it's not your turn, wait....");
//
//    }
//
//    @Override
//    public void chosenCards(ChosenCardsModelEvent e) {
//        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
//            ArrayList<String> card = new ArrayList<>();
//            for (int i = 0; i < e.getChosenCards().size(); i++)
//                card.add(e.getChosenCards().get(i));
//
//            SetUpMessage message = new SetUpMessage(SetUpType.CHALLENGERCARDS, card);
//            sendMessage(message);
//        }
//    }
//
//    @Override
//    public void setCard(SetCardModelEvent e) {
//        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
//            ArrayList<String> card = new ArrayList<>();
//            card.add(e.getCardName());
//            SetUpMessage message = new SetUpMessage(SetUpType.CHOSENCARD, card);
//            sendMessage(message);
//        }
//    }
//
//    @Override
//    public void defeatPlayer(PlayerDefeatModelEvent e) {
//    }
//
//    @Override
//    public void fullInfo(FullInfoModelEvent e) {
//
//        System.out.println("Scatta il full info model event");
//        /*
//        InfoMessage message = new InfoMessage(e.getType(), e.getPlayers(), e.getBoard());
//        sendMessage(message);*/
//    }
//
//    @Override
//    public void newCardsTurn(NewChoseCardTurnModelEvent e) {
//        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
//            ArrayList<String> cards = new ArrayList<>();
//            for (int i = 0; i < e.getCards().size(); i++)
//                cards.add(e.getCards().get(i));
//
//            SetUpMessage message = new SetUpMessage(SetUpType.NEWTURNCARD, cards);
//            sendMessage(message);
//        }else{
//            sendMessage("it's not your turn, wait....");
//        }
//    }
//
//    @Override
//    public void winGame(WinModelEvent e) {
//        System.out.println(e.getPlayer().getNickName());
//        System.out.println("win");
//    }
//
//    @Override
//    public void undo(UndoModelEvent e) {
//
//    }


}

