

package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

//import it.polimi.ingsw.client.cli.Color;
import it.polimi.ingsw.client.cli.ControllerResponseVisitor;
import it.polimi.ingsw.client.cli.ModelEventVisitor;
import it.polimi.ingsw.client.cli.SetUpMessageVisitor;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.PingSetUpMessage;
import it.polimi.ingsw.server.message.SetUpMessage;
import it.polimi.ingsw.server.message.SetUpType;
import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.*;

public class ClientConnection implements ViewEventListener, SignUpListener, Runnable {

    private Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream socketOut;
    private ModelEventVisitor modelVisitor;
    private ControllerResponseVisitor controllerVisitor;
    private SetUpMessageVisitor setUpVisitor;


//previously ClientGuiConnection
    public ClientConnection(String ip, int port, ModelEventVisitor visitor, ControllerResponseVisitor responseVisitor, SetUpMessageVisitor setUpVisitor){
        this.ip = ip;
        this.port = port;
        this.modelVisitor=visitor;
        this.controllerVisitor=responseVisitor;
        this.setUpVisitor=setUpVisitor;
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

                    while (isActive()) {
                        Object message = socketIn.readObject();
                        Thread t = new Thread( ()-> {
                            if (message instanceof ModelEvent) {
                                ModelEvent event = (ModelEvent) message;
                                event.accept(modelVisitor);
                            } else if (message instanceof ControllerResponse) {
                                ControllerResponse event = (ControllerResponse) message;
                                event.accept(controllerVisitor);

                            } else if (message instanceof SetUpMessage) {
                                SetUpMessage event = (SetUpMessage) message;
                                if (event.getSetUpType().equals(SetUpType.SIGN_UP)) {

                                        event.accept(setUpVisitor);
                                }

                            } else if (message instanceof ConnectionMessage) {
                                ConnectionMessage event = (ConnectionMessage) message;
                                if (event.getType().equals(ConnectionMessage.Type.PING)) {
                                    asyncSend(new ConnectionMessage(ConnectionMessage.Type.PONG));
                                } else if (event.getType().equals(ConnectionMessage.Type.DISCONNECTION)) {
                                    setActive(false);
                                    System.out.println("End game, player disconnected");
                                    //event.accept(setUpVisitor);
                                }
                            } else if(message instanceof String){
                                System.err.println(message);
                            } else {
                                throw new IllegalArgumentException(message.getClass().toString());
                            }
                        });
                        t.start();
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
            System.out.println("tutto ok");
        }

    }

    /*public void startMyTimer() {

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
    }*/


    public void run() {
        try{

            socket = new Socket(ip, port);
            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            //startMyTimer();
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
    public void handleEvent(ViewEvent e){
        asyncSend(e);
    }

    @Override
    public void handleEvent(SignUpMessage evt) {
        asyncSend(evt);
    }

/*
    @Override
    public void move(MovementGuiEvent e) {
        //OperationMessage message = new OperationMessage(Operation.MOVE, e.getStartPosition(), e.getDestinationPosition(), false);
        ViewEvent message = new MoveViewEvent(null, e.getStartPosition(), e.getDestinationPosition());
        asyncSend(message);
    }

    @Override
    public void sendLobbyEvent(LobbyGuiEvent e) {
        SignUpMessage message = new SignUpMessage(e.getNickname(), e.getNumPlayers());
        asyncSend(message);
    }

    @Override
    public void build(BuildGuiEvent e) {
//        OperationMessage message = new OperationMessage(Operation.BUILD, e.getStartPosition(), e.getDestinationPosition(), e.isDome());
//        System.out.println(message.getStartPosition().getX());
//        System.out.println(message.getStartPosition().getY());
//        System.out.println(message.getDestPosition().getX());
//        System.out.println(message.getDestPosition().getY());



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

    */

}

