package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

//import it.polimi.ingsw.client.cli.Color;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.server.message.*;
import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.*;

public class ClientConnection implements ViewEventListener, SignUpListener, Runnable {

    private Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream socketOut;
//    private ModelEventVisitor modelVisitor;
//    private ControllerResponseVisitor controllerVisitor;
//    private SetUpMessageVisitor setUpVisitor;
    private Queue<Object> toSend = new LinkedBlockingQueue<>();
    private Queue<GameMessage> gameMessages = new LinkedBlockingQueue<>();


//previously ClientGuiConnection
    public ClientConnection(String ip, int port){
        this.ip = ip;
        this.port = port;
//        this.toSend = toSend;
//        this.readMessages = readMessages;
    }

    private boolean active = true;

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    public void asyncReadFromSocket(final ObjectInputStream socketIn){
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
                try {
//                    while (isActive()) {
//                        if(socketIn.available()>0){
                            Object message = socketIn.readObject();
                            if(message instanceof GameMessage){
                                synchronized (this) {
                                    gameMessages.add((GameMessage) message);
                                    this.notifyAll();
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
                            //TODO remove
                            } else if(message instanceof String){
                                System.err.println(message);
                            } else {
                                throw new IllegalArgumentException(message.getClass().toString());
                            }
//                    if (message instanceof ModelEvent) {
//                        ModelEvent event = (ModelEvent) message;
//                        event.accept(modelVisitor);
//                    } else if (message instanceof ControllerResponse) {
//                        ControllerResponse event = (ControllerResponse) message;
//                        event.accept(controllerVisitor);
//
//                    } else if (message instanceof SetUpMessage) {
//                        SetUpMessage event = (SetUpMessage) message;
//                        if (event.getSetUpType().equals(SetUpType.SIGN_UP)) {
//                            event.accept(setUpVisitor);
//                        }
//
//                    } else if (message instanceof ConnectionMessage) {
//                        ConnectionMessage event = (ConnectionMessage) message;
//                        if (event.getType().equals(ConnectionMessage.Type.PING)) {
//                            asyncSend(new ConnectionMessage(ConnectionMessage.Type.PONG));
//                        } else if (event.getType().equals(ConnectionMessage.Type.DISCONNECTION)) {
//                            setActive(false);
//                            System.out.println("End game, player disconnected");
//                            //event.accept(setUpVisitor);
//                        }
//                    } else if(message instanceof String){
//                        System.err.println(message);
//                    } else {
//                        throw new IllegalArgumentException(message.getClass().toString());
//                    }


                } catch (Exception e){
                    setActive(false);
                }
//            }
//        });
//        t.start();
//        return t;
    }


    public void asyncSend(final Object message){
        toSend.add(message);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                send(message);
//            }
//        }).start();
    }

    public GameMessage pollReadMessages(){
        return gameMessages.poll();
    }

    private synchronized void send(Object message) {
        try {
            socketOut.reset();
            socketOut.writeObject(message);
            socketOut.flush();
        } catch(IOException e){
            System.out.println(e);
        }

    }
    @Override
    public void run() {
        try{

            socket = new Socket(ip, port);
            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            //startMyTimer();
            while (isActive()) {
                if(socketIn.available()>0){
                    asyncReadFromSocket(socketIn);
                }
                Object message = toSend.poll();
                if(message!=null) {
                    send(message);
                }
            }
//            Thread t0 = asyncRead...;
//            t0.join();

        } catch(NoSuchElementException e){
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

}

