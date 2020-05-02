package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.message.OperationMessage;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.message.SetUpMessage;
import it.polimi.ingsw.message.SetUpType;
//import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.event.*;
//import it.polimi.ingsw.server.ClientConnection;

public class RemoteView extends ViewEventEmitter implements ViewEventListener, ModelEventListener {

    private ClientConnection clientConnection;
    private Player player;

    public RemoteView(Player player, ClientConnection c) {
        this.player=player;
        this.clientConnection = c;
        clientConnection.addObserver(this);
    }

    public String getNickName(){
        return player.getNickName();
    }

    public Player getPlayer(){
        return player;
    }

    public void sendMessage(Object message){

        clientConnection.asyncSend(message);
    }


    @Override
    public void handleEvent(Object message) {

        if(message instanceof OperationMessage) {
            OperationMessage messaggio = (OperationMessage) message;
            System.out.println(messaggio.getType());
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
        }
            /*SetUpMessage messaggio = (SetUpMessage)message;
            if (messaggio.getType() == SetUpType.FIRSTPLAYER){
                FirstPlayerViewEvent e = new FirstPlayerViewEvent(this, messaggio);
                emitEvent(e);
            } else if(messaggio.getType() == SetUpType.CHOSENCARD){
                CardViewEvent e = new CardViewEvent(this, messaggio.getMessage());
                emitEvent(e);
             else if(messaggio.getType() == SetUpType.CHALLENGERCARDS){
                UndoViewEvent e = new UndoViewEvent(this, messaggio);
                emitEvent(e);
            }
*/
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
        System.out.println(e.getStartPosition().getX());
        System.out.println(e.getStartPosition().getY());
        System.out.println(e.getDestinationPosition().getX());
        System.out.println(e.getDestinationPosition().getY());
    }

    @Override
    public void build(BuildWorkerModelEvent e) {

    }

    @Override
    public void place(PlaceWorkerModelEvent e) {
        System.out.println("Remote View place Position");
        System.out.println(e.getPlacePosition().getX());
        System.out.println(e.getPlacePosition().getY());
       /*OperationMessage message = new OperationMessage(Operation.PLACE, e.getPlacePosition(), null , false);
        sendMessage(message);*/
    }

    @Override
    public void newTurn(NewTurnModelEvent e) {

        System.out.println("Remote View: turn player:");
        System.out.println(e.getPlayer().getNickName());
    }

    @Override
    public void chosenCards(ChosenCardsModelEvent e) {

    }

    @Override
    public void setCard(SetCardModelEvent e) {

    }

    @Override
    public void removePlayer(PlayerRemovalModelEvent e) {
    }


}

