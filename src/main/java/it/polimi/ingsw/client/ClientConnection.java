package it.polimi.ingsw.client;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.event.Message;
import it.polimi.ingsw.server.message.*;
import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.*;

public class ClientConnection extends ClientConnectionEventEmitter implements ViewEventListener, SignUpListener /*, Runnable*/ {

    private Socket socket;
    private String ip;
    private int port;
    private final Queue<Object> toSend = new LinkedBlockingQueue<>();
    private final Queue<Message> gameMessages = new LinkedBlockingQueue<>();
    private MessageVisitor gameMessageVisitor;
    private ExecutorService connectionThreads = Executors.newFixedThreadPool(3);
    //private TimeoutCounter timerTask;



//previously ClientGuiConnection
    public ClientConnection(String ip, int port, MessageVisitor gameMessageVisitor){
        this.ip = ip;
        this.port = port;
        this.gameMessageVisitor = gameMessageVisitor;
//        this.toSend = toSend;
//        this.readMessages = readMessages;
    }

    private boolean active = true;

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void setActive(boolean active){
        this.active = active;
    }

    public synchronized void closeConnection() {
        try {
            active = false;
            connectionThreads.shutdownNow();

            if(socket!=null)
                socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }

    }


    private void send(final Object message){
        synchronized (toSend) {
            toSend.add(message);
            toSend.notifyAll();
        }

    }



    private Message pollReadMessages(){
        synchronized (gameMessages) {
            Message message = null;
            while(((message = gameMessages.poll()) == null) && isActive()){
                try {
                    gameMessages.wait();
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    return null;
                }
            }

            return message;

        }
    }

    public void run() {
        try {

            socket = new Socket(ip, port);
            socket.setSoTimeout(10000);
            startTimerPing();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            ObjectInputStream socketIn = new ObjectInputStream(inputStream);
            ObjectOutputStream socketOut = new ObjectOutputStream(outputStream);
            connectionThreads.submit(new ObjectInputRunnable(socketIn));
            connectionThreads.submit(new ObjectOutputRunnable(socketOut));
            connectionThreads.submit(new ClientMessageReader());


        } catch (IOException e) {
            new ClientConnectionEvent(ClientConnectionEvent.Reason.ERROR_ON_THE_SOCKET).accept(gameMessageVisitor);
        } finally {

//            try {
//                if(socket != null) {
//                    socket.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }


    private void startTimerPing() {
        java.util.Timer timer = new Timer();
        int delta = 2000;
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                if (isActive()){
                    send(new ConnectionMessage(ConnectionMessage.Type.PONG));
                }else {
                    new ClientConnectionEvent(ClientConnectionEvent.Reason.PING_FAIL).accept(gameMessageVisitor);
                    timer.cancel();
                }
            }
        }, 2000, delta);

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
        private final ObjectInputStream socketIn;
        public ObjectInputRunnable(ObjectInputStream socketIn){
            this.socketIn = socketIn;
        }
        @Override
        public void run() {
            asyncReadFromSocket(socketIn);
        }
        public synchronized void asyncReadFromSocket(final ObjectInputStream socketIn){

            try {
                while (isActive()) {
                    Object message = socketIn.readObject();
                    if (message instanceof GameMessage) {
                        synchronized (gameMessages) {
                            gameMessages.add((GameMessage) message);
                            gameMessages.notifyAll();
                        }
                         if (message instanceof ConnectionMessage) {
                            ConnectionMessage event = (ConnectionMessage) message;
                             if (event.getType().equals(ConnectionMessage.Type.DISCONNECTION) || event.getType().equals(ConnectionMessage.Type.DISCONNECTION_TOO_MANY_PLAYERS)) {
                                 new ClientConnectionEvent(ClientConnectionEvent.Reason.CONNECTION_LOST).accept(gameMessageVisitor);
                                 closeConnection();
                             }


                        }
                        //TODO remove
                    } else if (message instanceof String) {
                        System.err.println(message);
                    } else {
                        throw new IllegalArgumentException(message.getClass().toString());
                    }
                }

            } catch (Exception e){
                setActive(false);

            }finally {
                try {
                    socketIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class ObjectOutputRunnable implements Runnable{
        private final ObjectOutputStream socketOut;
        public ObjectOutputRunnable(ObjectOutputStream socketOut){
            this.socketOut = socketOut;
        }
        @Override
        public void run() {
            //syncronized wait on toSend
                try {
                    while (isActive()) {
                        synchronized (toSend) {
                            Object message;
                            while ((message = toSend.poll()) == null) {
                                toSend.wait();
                            }
                            if (isActive()) {
                                send(message);
                            }
                        }
                    }

//                    System.out.println("Esco dal thread out message ");

                } catch (IOException e) {
                    new ClientConnectionEvent(ClientConnectionEvent.Reason.IO_EXCEPTION).accept(gameMessageVisitor);
                } catch (InterruptedException e) {
                    //emitEvent(new ClientConnectionEvent(ClientConnectionEvent.Reason.INTERRUPTED));
                    //QUANDO C'E' DISCONNESSIONE PLAYER ENTRA QUA
                    //new ClientConnectionEvent(ClientConnectionEvent.Reason.INTERRUPTED).accept(gameMessageVisitor);
                }finally {
                        try {
                            socketOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
        }

        private synchronized void send(Object message) throws IOException{
                socketOut.reset();
                socketOut.writeObject(message);
                socketOut.flush();
        }
    }

    private class ClientMessageReader implements Runnable{
        public ClientMessageReader(){}
        @Override
        public void run(){
            while(isActive()) {
                Message message;

                //Synchronized inside pollReadMessages()
                message = pollReadMessages();

                if(message == null){
                    return;
                }
                message.accept(gameMessageVisitor);
                }
            }

        }
}




