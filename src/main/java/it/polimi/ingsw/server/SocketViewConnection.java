
package it.polimi.ingsw.server;

import it.polimi.ingsw.message.PingMessage;
import it.polimi.ingsw.message.SetUpMessage;
import it.polimi.ingsw.message.SetUpType;
import it.polimi.ingsw.message.SignUpMessage;
import it.polimi.ingsw.view.event.ViewEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

public class SocketViewConnection extends ViewEventObservable implements ViewConnection, Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Server server;
    static final int maxRetries = 10;
    private boolean active = true;

    public SocketViewConnection(Socket socket, Server server)  {
        this.socket = socket;
        this.server = server;

    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();
        server.clientCloseConnection(this);
        System.out.println("Disconnect player done!");
    }

    @Override
    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    Object inputObject = socketIn.readObject();
                    /*if(inputObject instanceof String){
                        System.out.println((String) inputObject);
                    }
                    else*/ if(inputObject instanceof SignUpMessage) {
                        SignUpMessage message = (SignUpMessage) inputObject;
                        server.checkUpRegistration(message.getNickName(), message.getNumPlayers(), this);
                    }
                    else if(inputObject instanceof ViewEvent){
                        ViewEvent event = (ViewEvent) inputObject;
                        eventNotify(event);
                    }
                }
            } catch (IOException | NoSuchElementException | ClassNotFoundException e) {
                //System.err.println("Error!" + e.getMessage());
            }
        });
        t.start();
        return t;
    }





    public void startMyTimer() {

        Timer timer = new Timer();
        TimeOutCheckerInterface timeOutChecker = () -> {
            if (isActive()){
                send(new PingMessage(SetUpType.PING));
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

   public void createObjectStream()  {
       try {
           out = new ObjectOutputStream(socket.getOutputStream());
           in = new ObjectInputStream(socket.getInputStream());
       }catch(IOException e){
           e.getMessage();
       }
    }


    @Override
    public void run(){

        try{
            startMyTimer();
            socket.setSoTimeout(20000);
            Thread t0 = asyncReadFromSocket(in);
            t0.join();
            out.close();
            in.close();
        } catch (IOException | NoSuchElementException | InterruptedException e) {
            System.err.println("Error! Entra qui" + e.getMessage());
            System.out.println("ciaooo");
        } finally{
            close();
        }



    }


}

