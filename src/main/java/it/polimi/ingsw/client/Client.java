

package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import it.polimi.ingsw.message.*;
import it.polimi.ingsw.model.Operation;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.view.event.PlaceViewEvent;

import java.util.Scanner;

public class Client implements FirstPlayerEventListener, SetCardEventListener, ChalCardsEventListener, MovementEventListener, LobbyEventListener,BuildEventListener, PlaceEventListener, EndTurnEventListener, UndoGuiEventListener, Runnable {

    private Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream socketOut;


    public Client(String ip, int port){
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
                    while (isActive()) {
                        Object message = socketIn.readObject();
                        if(message instanceof String){
                            System.out.println((String)message);
                        }
                        else if (message instanceof OperationMessage){
                            OperationMessage messaggio = (OperationMessage)message;
                            if (messaggio.getType().equals(Operation.PLACE)) {
                                Position startPosition;
                                try {
                                    startPosition = new Position(messaggio.getStartPosition().getX(), messaggio.getStartPosition().getY());
                                    System.out.println("you have place here");
                                    System.out.println(messaggio.getStartPosition().getX());
                                    System.out.println(messaggio.getStartPosition().getY());


                                }catch (PositionOutOfBoundsException e){

                                }
                            }
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



   /* public Thread asyncWriteToSocket(final Scanner stdin, final ObjectOutputStream socketOut){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        String inputLine = stdin.nextLine();
                        LobbyViewEvent lobbyEvent = new LobbyViewEvent(inputLine);
                        socketOut.writeObject(lobbyEvent);
                        socketOut.flush();
                    }
                }catch(Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }*/



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


    public void run() {


        try{
            socket = new Socket(ip, port);
            //System.out.println("Connection established");
            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            //Scanner stdin = new Scanner(System.in);
            Thread t0 = asyncReadFromSocket(socketIn);
            //Thread t1 = asyncWriteToSocket(stdin, socketOut);
            t0.join();
            //t1.join();
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

        OperationMessage message = new OperationMessage(Operation.MOVE, e.getStartPosition(), e.getDestinationPosition(), false);
        asyncSend(message);
    }

    @Override
    public void sendLobbyEvent(LobbyGuiEvent e) {
        LobbyMessage message = new LobbyMessage(e.getNickname());
        asyncSend(message);
    }

    @Override
    public void build(BuildGuiEvent e) {
        OperationMessage message = new OperationMessage(Operation.BUILD, e.getStartPosition(), e.getDestinationPosition(), e.isDome());
        System.out.println(message.getStartPosition().getX());
        System.out.println(message.getStartPosition().getY());
        System.out.println(message.getDestPosition().getX());
        System.out.println(message.getDestPosition().getY());

        asyncSend(message);
    }

    @Override
    public void place(PlaceGuiEvent e) {
        OperationMessage message = new OperationMessage(Operation.PLACE, e.getPosition(), null, false);
        asyncSend(message);
    }

    @Override
    public void endTurn(EndTurnGuiEvent e) {
        SetUpMessage message = new SetUpMessage(SetUpType.ENDTURN, null);
        asyncSend(message);
    }

    @Override
    public void undo(UndoGuiEvent e) {
        SetUpMessage message = new SetUpMessage(SetUpType.UNDO, null);
        asyncSend(message);
    }

    @Override
    public void chalCards(ChalCardsGuiEvent e) {
        SetUpMessage message = new SetUpMessage(SetUpType.CHALLENGERCARDS, e.getCards());
        asyncSend(message);
    }

    @Override
    public void setCard(SetCardGuiEvent e) {
        ArrayList<String> card= new ArrayList<>();
        card.add(e.getCard());
        SetUpMessage message = new SetUpMessage(SetUpType.CHOSENCARD, card);
        asyncSend(message);
    }

    @Override
    public void firstPlayer(FirstPlayerGuiEvent e) {
        ArrayList<String> name= new ArrayList<>();
        name.add(e.getFirstPlayer());
        SetUpMessage message = new SetUpMessage(SetUpType.FIRSTPLAYER, name );
        asyncSend(message);
    }
}

