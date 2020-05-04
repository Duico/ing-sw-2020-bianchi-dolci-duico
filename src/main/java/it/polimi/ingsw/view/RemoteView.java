package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.server.ViewConnection;
import it.polimi.ingsw.view.event.*;
import java.util.ArrayList;

public class RemoteView extends ViewEventEmitter implements ViewEventListener, ModelEventListener {

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
    public void handleEvent(Object message) {

        if(message instanceof OperationMessage) {
            OperationMessage messaggio = (OperationMessage) message;

            if (messaggio.getType().equals(Operation.MOVE)) {
                Position startPosition;
                Position destPosition;
                try {
                    startPosition = new Position(messaggio.getStartPosition().getX(), messaggio.getStartPosition().getY());
                    destPosition = new Position(messaggio.getDestPosition().getX(), messaggio.getDestPosition().getY());
                    MoveViewEvent e = new MoveViewEvent(this, startPosition, destPosition);
                    emitEvent(e);

                }catch (PositionOutOfBoundsException e){

                }

            } else if (messaggio.getType().equals(Operation.BUILD)) {
                Position startPosition;
                Position destPosition;
                boolean isDome;
                try {
                    startPosition = new Position(messaggio.getStartPosition().getX(), messaggio.getStartPosition().getY());
                    destPosition = new Position(messaggio.getDestPosition().getX(), messaggio.getDestPosition().getY());
                    isDome = messaggio.isDome();
                    BuildViewEvent e = new BuildViewEvent(this, startPosition, destPosition, isDome);
                    emitEvent(e);

                }catch (PositionOutOfBoundsException e){

                }

            } else if (messaggio.getType().equals(Operation.PLACE)) {
                Position startPosition;
                try {
                    startPosition = new Position(messaggio.getStartPosition().getX(), messaggio.getStartPosition().getY());
                    PlaceViewEvent e = new PlaceViewEvent(this, startPosition);

                    emitEvent(e);
                }catch (PositionOutOfBoundsException e){

                }
            }


        }else if(message instanceof SetUpMessage){
            SetUpMessage messaggio = (SetUpMessage)message;
            if (messaggio.getType() == SetUpType.ENDTURN){
                EndTurnViewEvent e = new EndTurnViewEvent(this);
                emitEvent(e);
            }else if(messaggio.getType() == SetUpType.UNDO){
            UndoViewEvent e = new UndoViewEvent(this);
            emitEvent(e);
            } else if(messaggio.getType() == SetUpType.CHALLENGERCARDS){
                ChallengerCardViewEvent e = new ChallengerCardViewEvent(this, messaggio.getMessage());
                emitEvent(e);
            } else if(messaggio.getType() == SetUpType.CHOSENCARD){
            CardViewEvent e = new CardViewEvent(this, messaggio.getMessage().get(0));
            emitEvent(e);
            } else if (messaggio.getType() == SetUpType.FIRSTPLAYER) {
                FirstPlayerViewEvent e = new FirstPlayerViewEvent(this, messaggio.getMessage().get(0));
                emitEvent(e);
            }
        }

        else {
            throw new IllegalArgumentException();
        }
    }



    public void eventResponse(ControllerResponse response){
        //send to Client View
    }



    @Override
    public void movement(MoveWorkerModelEvent e) {
        System.out.println("Remote View Movement");
        /*OperationMessage message = new OperationMessage(Operation.MOVE, e.getStartPosition(), e.getDestinationPosition() , false);
        sendMessage(message);*/
    }

    @Override
    public void build(BuildWorkerModelEvent e) {
        /*OperationMessage message = new OperationMessage(Operation.BUILD, e.getStartPosition(), e.getDestinationPosition() , e.isDome());
        sendMessage(message);*/

    }

    @Override
    public void place(PlaceWorkerModelEvent e) {
        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
            OperationMessage message = new OperationMessage(Operation.PLACE, e.getPlacePosition(), null , false);
            sendMessage(message);
        }


    }

    @Override
    public void newTurn(NewTurnModelEvent e) {
        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
            SetUpMessage message = new SetUpMessage(SetUpType.NEWTURN, null);
            sendMessage(message);
        }else
            sendMessage("it's not your turn, wait....");

    }

    @Override
    public void chosenCards(ChosenCardsModelEvent e) {
        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
            ArrayList<String> card = new ArrayList<>();
            for (int i = 0; i < e.getChosenCards().size(); i++)
                card.add(e.getChosenCards().get(i));

            SetUpMessage message = new SetUpMessage(SetUpType.CHALLENGERCARDS, card);
            sendMessage(message);
        }
    }

    @Override
    public void setCard(SetCardModelEvent e) {
        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
            ArrayList<String> card = new ArrayList<>();
            card.add(e.getCardName());
            SetUpMessage message = new SetUpMessage(SetUpType.CHOSENCARD, card);
            sendMessage(message);
        }
    }

    @Override
    public void defeatPlayer(PlayerDefeatModelEvent e) {
    }

    @Override
    public void fullInfo(FullInfoModelEvent e) {

        System.out.println("Scatta il full info model event");
        /*
        InfoMessage message = new InfoMessage(e.getType(), e.getPlayers(), e.getBoard());
        sendMessage(message);*/
    }

    @Override
    public void newCardsTurn(NewChoseCardTurnModelEvent e) {
        if(e.getPlayer().getNickName().equals(getPlayer().getNickName())) {
            ArrayList<String> cards = new ArrayList<>();
            for (int i = 0; i < e.getCards().size(); i++)
                cards.add(e.getCards().get(i));

            SetUpMessage message = new SetUpMessage(SetUpType.NEWTURNCARD, cards);
            sendMessage(message);
        }else{
            sendMessage("it's not your turn, wait....");
        }
    }

    @Override
    public void winGame(WinModelEvent e) {
        System.out.println(e.getPlayer().getNickName());
        System.out.println("win");
    }

    @Override
    public void undo(UndoModelEvent e) {

    }


}

