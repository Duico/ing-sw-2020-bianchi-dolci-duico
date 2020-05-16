package it.polimi.ingsw.client;

import java.io.*;
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

public class ClientConnection implements ViewEventListener, SignUpListener /*, Runnable*/ {

    private Socket socket;
    private String ip;
    private int port;
//    private ObjectOutputStream socketOut;
//    private ModelEventVisitor modelVisitor;
//    private ControllerResponseVisitor controllerVisitor;
//    private SetUpMessageVisitor setUpVisitor;
    private final Queue<Object> toSend = new LinkedBlockingQueue<>();
    private final Queue<GameMessage> gameMessages = new LinkedBlockingQueue<>();


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


    public void send(final Object message){
        synchronized (toSend) {
            toSend.add(message);
            toSend.notifyAll();
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                send(message);
//            }
//        }).start();
    }

    public GameMessage pollReadMessages(){
        synchronized (gameMessages) {
            GameMessage message = null;
            while((message = gameMessages.poll()) == null){
                try {
                    gameMessages.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return message;
        }
    }

    public void run() {
        try{

            socket = new Socket(ip, port);
            //startMyTimer();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            ObjectInputStream socketIn = new ObjectInputStream(inputStream);
            ObjectOutputStream socketOut = new ObjectOutputStream(outputStream);
            Thread objectInputThread = new Thread(new ObjectInputRunnable(socketIn));
            Thread objectOutputThread = new Thread(new ObjectOutputRunnable(socketOut));

            objectInputThread.start();
            objectOutputThread.start();
            objectInputThread.join();
            objectOutputThread.join();
//            Thread t0 = asyncRead...;
//            t0.join();

        } catch(NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void handleEvent(ViewEvent e){
        send(e);
    }

    @Override
    public void handleEvent(SignUpMessage evt) {
        send(evt);
    }

    private class ObjectInputRunnable implements Runnable{
        private ObjectInputStream socketIn;
        public ObjectInputRunnable(ObjectInputStream socketIn){
            this.socketIn = socketIn;
        }
        @Override
        public void run() {
            asyncReadFromSocket(socketIn);
        }
        public synchronized void asyncReadFromSocket(final ObjectInputStream socketIn){
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
            try {
                while (isActive()) {
//                        if(socketIn.available()>0){
                    Object message = socketIn.readObject();
                    if (message instanceof GameMessage) {
                        synchronized (gameMessages) {
                            gameMessages.add((GameMessage) message);
                            gameMessages.notifyAll();
                        }
                    } else if (message instanceof ConnectionMessage) {
                        ConnectionMessage event = (ConnectionMessage) message;
                        if (event.getType().equals(ConnectionMessage.Type.PING)) {
                            send(new ConnectionMessage(ConnectionMessage.Type.PONG));
                        } else if (event.getType().equals(ConnectionMessage.Type.DISCONNECTION)) {
                            setActive(false);
                            System.out.println("End game, player disconnected");
                            //event.accept(setUpVisitor);
                        }
                        //TODO remove
                    } else if (message instanceof String) {
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
                }

            } catch (Exception e){
                setActive(false);
            }
//            }
//        });
//        t.start();
//        return t;
        }
    }
    private class ObjectOutputRunnable implements Runnable{
        private ObjectOutputStream socketOut;
        public ObjectOutputRunnable(ObjectOutputStream socketOut){
            this.socketOut = socketOut;
        }
        @Override
        public void run() {
            //syncronized wait on toSend
            while (isActive()) {
                try {
                    synchronized (toSend) {
                        Object message;
                        while ((message = toSend.poll()) == null){
                            toSend.wait();
                        }
                        if (message != null) {
                            send(message);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
    }

}

