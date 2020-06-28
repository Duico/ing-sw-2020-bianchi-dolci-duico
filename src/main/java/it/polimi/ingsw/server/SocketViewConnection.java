
package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.*;
import it.polimi.ingsw.client.message.SignUpMessage;
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

    /**
     * @param socket socket for the connection
     * @param server server
     */
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

    @Override
    public synchronized void send(Object message) {
        try {
            out.reset();
        } catch(IOException e){
            System.out.println("Send of "+message.getClass()+" failed");

        }
        try{
            out.writeObject(message);
            out.flush();
        }catch(IOException e){

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
        System.out.println("Gracefully closing connection to remaining player.");
    }


    @Override
    public void removeDefeatedPlayer() {
        server.removeFromGameConnectionList(this);
    }

    /**
     * creates a thread and reads from input stream
     * @param socketIn input stream of socket
     * @return thread created
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    Object inputObject = socketIn.readObject();
                    if(inputObject instanceof String){
                        System.out.println((String) inputObject);
                    }
                    else if(inputObject instanceof SignUpMessage) {
                        SignUpMessage message = (SignUpMessage) inputObject;
                        server.checkUpRegistration(message.getNickName(), message.getNumPlayers(), message.isPersistency(), this);
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


    /**
     * sends ping message to client and restarts ping timer
     */
    public void startPingTimer() {
        Timer timer = new Timer();
        TimeOutCheckerInterface timeOutChecker = () -> {
            if (isActive()){
                this.send(new ConnectionMessage(ConnectionMessage.Type.PING));
                return false;
            }else{
                System.out.println("The connection is inactive");
                return true;
            }
        };

        TimerTask task = new TimeoutCounter(timeOutChecker);
        int intialDelay = 2000;
        int delta = 2000;
        timer.schedule(task, intialDelay, delta);
    }

   public void createObjectStream() throws IOException{

           out = new ObjectOutputStream(socket.getOutputStream());
           in = new ObjectInputStream(socket.getInputStream());

    }


    @Override
    public void run(){
        try{

            socket.setSoTimeout(8000);
            startPingTimer();
            Thread t0 = asyncReadFromSocket(in);
            t0.join();
            out.close();
            in.close();
        } catch (IOException | NoSuchElementException | InterruptedException e) {
            System.err.println("Error! " + e.getMessage());
        } finally{
            close();
        }
    }


}

