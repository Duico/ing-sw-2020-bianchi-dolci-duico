

package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import it.polimi.ingsw.client.cli.Color;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.server.TimeOutCheckerInterface;
import it.polimi.ingsw.server.TimeoutCounter;
import it.polimi.ingsw.view.event.*;

public class ClientGuiConnection implements FirstPlayerEventListener, SetCardEventListener, ChalCardsEventListener, MovementEventListener, LobbyEventListener,BuildEventListener, PlaceEventListener, EndTurnEventListener, UndoGuiEventListener, Runnable {

    private Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream socketOut;


    public ClientGuiConnection(String ip, int port){
        this.ip = ip;
        this.port = port;

    }

    private boolean active = true;

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //SORRY @Tia

                    while (isActive()) {
                        Object message = socketIn.readObject();
                        if(message instanceof String){
                            System.out.println(Color.YELLOW_UNDERLINED.escape("X Tia: Implementare il visitor pattern anche nella ClientGuiConnection"));
                            System.out.println((String)message);

//                        }else if (message instanceof SetUpMessage){
//                            SetUpMessage messaggio = (SetUpMessage) message;
//                            if(messaggio.getType().equals(SetUpType.NEWTURNCARD)){
//                                System.out.println("It's your turn, chose:");
//                                for (int i=0;i<messaggio.getMessage().size();i++)
//                                    System.out.println(messaggio.getMessage().get(i));
//                            }
//                            if(messaggio.getType().equals(SetUpType.CHOSENCARD)){
//                                System.out.println("Correct chose card");
//                                for (int i=0;i<messaggio.getMessage().size();i++)
//                                    System.out.println(messaggio.getMessage().get(i));
//                            }
//                            if(messaggio.getType().equals(SetUpType.CHALLENGERCARDS)){
//                                System.out.println("Correct choses cards");
//                                for (int i=0;i<messaggio.getMessage().size();i++)
//                                    System.out.println(messaggio.getMessage().get(i));
//                            }
//                            if(messaggio.getType().equals(SetUpType.NEWTURN)){
//                                System.out.println("It's your turn, place");
//                            }
//
//                        }
//                        else if (message instanceof OperationMessage){
//                            OperationMessage messaggio = (OperationMessage)message;
//                            if (messaggio.getType().equals(Operation.PLACE)) {
//                                Position startPosition;
//                                try {
//                                    startPosition = new Position(messaggio.getStartPosition().getX(), messaggio.getStartPosition().getY());
//                                    System.out.println("you have place here");
//                                    System.out.println(messaggio.getStartPosition().getX());
//                                    System.out.println(messaggio.getStartPosition().getY());
//
//
//                                }catch (PositionOutOfBoundsException e){
//
//                                }
//                            }
//                        }
//                        else if (message instanceof ErrorMessage){
//                            ErrorMessage messaggio = (ErrorMessage) message;
//                            System.out.println(messaggio.getMessage());
//                            socketIn.close();
                        }
                        else {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }


    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    private synchronized void send(Object message) {
        try {
            socketOut.reset();
            socketOut.writeObject(message);
            socketOut.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }

    }

    public void startMyTimer() {

        Timer timer = new Timer();
        TimeOutCheckerInterface timeOutChecker = () -> {
            if (isActive()){
                send("ping");
                return false;
            }else{
                System.out.println("The connection is inactive");
                return true;
            }
        };

            TimerTask task = new TimeoutCounter(timeOutChecker);
            int intialDelay = 3000;
            int delta = 3000;
            timer.schedule(task, intialDelay, delta);
    }


    public void run() {
        try{

            socket = new Socket(ip, port);
            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            startMyTimer();
            Thread t0 = asyncReadFromSocket(socketIn);
            t0.join();

        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socketOut.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void move(MovementGuiEvent e) {
        //OperationMessage message = new OperationMessage(Operation.MOVE, e.getStartPosition(), e.getDestinationPosition(), false);
        ViewEvent message = new MoveViewEvent(null, e.getStartPosition(), e.getDestinationPosition());
        asyncSend(message);
    }

    @Override
    public void sendLobbyEvent(LobbyGuiEvent e) {
        LobbyMessage message = new LobbyMessage(e.getNickname(), e.getNumPlayers());
        asyncSend(message);
    }

    @Override
    public void build(BuildGuiEvent e) {
        /*OperationMessage message = new OperationMessage(Operation.BUILD, e.getStartPosition(), e.getDestinationPosition(), e.isDome());
        System.out.println(message.getStartPosition().getX());
        System.out.println(message.getStartPosition().getY());
        System.out.println(message.getDestPosition().getX());
        System.out.println(message.getDestPosition().getY());

         */

        ViewEvent message = new BuildViewEvent(null, e.getStartPosition(), e.getDestinationPosition(), e.isDome());
        asyncSend(message);
    }

    @Override
    public void place(PlaceGuiEvent e) {
        ViewEvent message = new PlaceViewEvent(null, e.getPosition());
        //OperationMessage message = new OperationMessage(Operation.PLACE, e.getPosition(), null, false);
        asyncSend(message);
    }

    @Override
    public void endTurn(EndTurnGuiEvent e) {
        //SetUpMessage message = new SetUpMessage(SetUpType.ENDTURN, null);
        ViewEvent message= new EndTurnViewEvent(null);
        asyncSend(message);
    }

    @Override
    public void undo(UndoGuiEvent e) {

        //SetUpMessage message = new SetUpMessage(SetUpType.UNDO, null);
        ViewEvent message = new UndoViewEvent(null);
        asyncSend(message);
    }

    @Override
    public void chalCards(ChalCardsGuiEvent e) {
        ViewEvent message = new ChallengerCardViewEvent(null, e.getCards());
        asyncSend(message);
    }

    @Override
    public void setCard(SetCardGuiEvent e) {
        ViewEvent message = new CardViewEvent(null, e.getCard());
        asyncSend(message);
    }

    @Override
    public void firstPlayer(FirstPlayerGuiEvent e) {
        ViewEvent message = new FirstPlayerViewEvent(null, e.getFirstPlayer());
        asyncSend(message);
    }
}

