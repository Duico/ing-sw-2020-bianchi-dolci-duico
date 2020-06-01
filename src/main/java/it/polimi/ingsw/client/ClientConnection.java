package it.polimi.ingsw.client;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

//import it.polimi.ingsw.client.cli.Color;
import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.event.Message;
import it.polimi.ingsw.server.TimeOutCheckerInterface;
import it.polimi.ingsw.server.TimeoutCounter;
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



//previously ClientGuiConnection
    public ClientConnection(String ip, int port, MessageVisitor gameMessageVisitor){
        this.ip = ip;
        this.port = port;
        this.gameMessageVisitor = gameMessageVisitor;
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

    /*public GameMessage pollReadMessages(){
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
    }*/

    public Message pollReadMessages(){
        synchronized (gameMessages) {
            Message message = null;
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
        try {

            socket = new Socket(ip, port);
            socket.setSoTimeout(6000);
            startPingTimer();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            ObjectInputStream socketIn = new ObjectInputStream(inputStream);
            ObjectOutputStream socketOut = new ObjectOutputStream(outputStream);
            Thread objectInputThread = new Thread(new ObjectInputRunnable(socketIn));
            Thread objectOutputThread = new Thread(new ObjectOutputRunnable(socketOut));
            Thread messageReader = new Thread(new ClientMessageReader());

            objectInputThread.start();
            objectOutputThread.start();
            messageReader.start();
            objectInputThread.join();
            objectOutputThread.join();
            messageReader.join();
//            Thread t0 = asyncRead...;
//            t0.join();

        } catch (IOException e) {
            //emitEvent(new ClientConnectionEvent(ClientConnectionEvent.Reason.ERROR_ON_THE_SOCKET));
            new ClientConnectionEvent(ClientConnectionEvent.Reason.ERROR_ON_THE_SOCKET).accept(gameMessageVisitor);
        }
         catch (InterruptedException e) {
            System.err.print("TODO handle gui interrupts");
        } finally {
            try {
                if(socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startPingTimer() {
        Timer timer = new Timer();
        TimeOutCheckerInterface timeOutChecker = () -> {
//            if (isActive()){
                send(new ConnectionMessage(ConnectionMessage.Type.PONG));
                return false;
//            }
//            else{
//                emitEvent(new ClientConnectionEvent(ClientConnectionEvent.Reason.PING_FAIL));
//                try {
//                    if(socket != null) {
//                        socket.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return true;
//            }
        };

        TimerTask task = new TimeoutCounter(timeOutChecker);
        int intialDelay = 1500;
        int delta = 2000;
        timer.schedule(task, intialDelay, delta);
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
                         if (message instanceof ConnectionMessage) {
                            ConnectionMessage event = (ConnectionMessage) message;
                             if (event.getType().equals(ConnectionMessage.Type.DISCONNECTION) || event.getType().equals(ConnectionMessage.Type.DISCONNECTION_TOO_MANY_PLAYERS)) {
                                 setActive(false);
                                 //event.accept(setUpVisitor);
                             }

                            /*if (event.getType().equals(ConnectionMessage.Type.PING)) {
//                                send(new ConnectionMessage(ConnectionMessage.Type.PONG));
                            } else if (event.getType().equals(ConnectionMessage.Type.DISCONNECTION)) {
                                //emitEvent(new ClientConnectionEvent(ClientConnectionEvent.Reason.DISCONNECTION));
                                setActive(false);
                                //event.accept(setUpVisitor);
                            }*/
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
//                System.out.println("LA metto io a false 2");
            }finally {
                try {
                    socketIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            }
//        });
//        t.start();
//        return t;
        }
    }

    /*private void emitEvent(ClientConnectionEvent evt){
        executeEventListeners(ClientConnectionEventListener.class, (listener) -> listener.handleEvent(evt) );
    }*/

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
                    //emitEvent(new ClientConnectionEvent(ClientConnectionEvent.Reason.IO_EXCEPTION));
                    new ClientConnectionEvent(ClientConnectionEvent.Reason.IO_EXCEPTION).accept(gameMessageVisitor);
                } catch (InterruptedException e) {
                    //emitEvent(new ClientConnectionEvent(ClientConnectionEvent.Reason.INTERRUPTED));
                    new ClientConnectionEvent(ClientConnectionEvent.Reason.INTERRUPTED).accept(gameMessageVisitor);
                }finally {
                        try {
                            socketOut.close();
                            //emitEvent(new ClientConnectionEvent(ClientConnectionEvent.Reason.PING_FAIL));
                            new ClientConnectionEvent(ClientConnectionEvent.Reason.PING_FAIL).accept(gameMessageVisitor);
                        } catch (IOException e) {
                            //emitEvent(new ClientConnectionEvent(ClientConnectionEvent.Reason.CLOSE_IO_EXCEPTION));
                            new ClientConnectionEvent(ClientConnectionEvent.Reason.CLOSE_IO_EXCEPTION).accept(gameMessageVisitor);
                        }
                    }
        }
        private synchronized void send(Object message) throws IOException{
//            try {
                socketOut.reset();
                socketOut.writeObject(message);
                socketOut.flush();
//            } catch(IOException e){
//                e.printStackTrace();
//            }

        }
    }

    private class ClientMessageReader implements Runnable{
        public ClientMessageReader(){}
        @Override
        public void run(){
            while(isActive()) {
                //GameMessage message;
                Message message;
                /*synchronized (gameMessages) {
                    while ( (message = pollReadMessages() ) == null){
                        try {
                            gameMessages.wait();
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }*/

                //Synchronized inside pollReadMessages()
                message = pollReadMessages();
                //message.accept(gameMessageVisitor);
                //emitEvent(message);
                message.accept(gameMessageVisitor);
                }
            }

        }
}




