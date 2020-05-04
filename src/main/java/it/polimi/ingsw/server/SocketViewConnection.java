
package it.polimi.ingsw.server;

import it.polimi.ingsw.message.LobbyMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

public class SocketViewConnection extends ObservableConnection implements ViewConnection, Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream input;
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
            System.err.println(e.getMessage());
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
                    if(inputObject instanceof String){

                    }
                    else if(inputObject instanceof LobbyMessage) {
                        LobbyMessage message = (LobbyMessage) inputObject;
                        checkUpRegistration(message.getNickName(), message.getNumPlayers());
                    }
                    else{
                        notify(inputObject);
                    }
                }
            } catch (IOException | NoSuchElementException | ClassNotFoundException e) {
                //System.err.println("Error!" + e.getMessage());
            }
        });
        t.start();
        return t;
    }



    public void checkUpRegistration(String nickName, int numPlayers){
        if(server.getNumPlayers()==0){
            server.addPlayer(nickName);
            if(!server.setNumPlayers(numPlayers)) {
                send("incorrect number of player");
                return;
            }
            server.lobby(this, nickName);
            server.adviseAllAttendances();
            send("Attendi gli avversari");
        } else if (server.addPlayer(nickName)) {
               send("Attendi gli avversari");
               server.lobby(this, nickName);
        } else{
            send("nickName already used");
        }
    }



    @Override
    public void run(){

        try{

            socket.setSoTimeout(20000);
            out = new ObjectOutputStream(socket.getOutputStream());
            if(server.createNewGame())
                send("Welcome, select number of player and nickname");
            else if(!server.createNewGame() && server.getNumPlayers()==0) {
                send("Please, attend the creation of the game....");
                server.addWaitingGame(this);
            }else{
                send("Welcome, select nickname");
            }


            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Thread t0 = asyncReadFromSocket(in);
            t0.join();
            out.close();
            in.close();
            socket.close();
        } catch (IOException | NoSuchElementException | InterruptedException e) {
            System.err.println("Error! Entra qui" + e.getMessage());
            System.out.println("ciaooo");

        } finally{
            close();
        }



    }


}

