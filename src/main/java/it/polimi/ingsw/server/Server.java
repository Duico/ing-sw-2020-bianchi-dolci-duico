
package it.polimi.ingsw.server;

import it.polimi.ingsw.server.message.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Lobby lobby;
//    private Game game;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(100);
    private Map<ViewConnection,String> waitingConnection = new LinkedHashMap<>();
    private ArrayList<ViewConnection> connections = new ArrayList<>();
    private boolean hasGameStarted = false;
    private boolean isPersistencyAvailable;

    public Server(Integer port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        System.out.println("Listening on 0.0.0.0:"+serverSocket.getLocalPort());
    }

    public synchronized void clientCloseConnection(ViewConnection c) {
        boolean isFirstConnection = c == getFirstConnection();
            if (isFirstConnection || (waitingConnection.containsKey(c) && hasGameStarted)) {
//                if(waitingConnection.containsKey(c)) {
                    waitingConnectionRemove(c);
                    connections.remove(c);

                    //ArrayList<ViewConnection> viewConnections = new ArrayList<>(waitingConnection.keySet());
                    for (ViewConnection connection : connections) {
                        connection.send(new ConnectionMessage(ConnectionMessage.Type.DISCONNECTION));
                    }
                    connections.clear();
                    //it is safe not to clear lobby.waitingPlayers if we unset the lobby later
                    waitingConnection.clear();
                    lobby.persistencySaveGame();
                    lobby.clearGame();
                    this.lobby = null;
                    System.out.println("game, lobby set to null");
                    hasGameStarted = false;
            } else {
                //client disconnected from lobby
//                if(waitingConnection.containsKey(c)) {
                    waitingConnectionRemove(c);
                    connections.remove(c);
//                }
            }


    }

    private synchronized void waitingConnectionRemove(ViewConnection c){
        lobby.removePlayerByName(waitingConnection.get(c));
        waitingConnection.remove(c);
    }


    public synchronized void createNewGame(){
//            if(!lobby.isSetGame()) {
                System.out.println("Players waiting: "+waitingConnection.size());
                Integer numPlayers = lobby.getNumPlayers();

                ArrayList<ViewConnection> viewConnections = new ArrayList<>(waitingConnection.keySet());
                ViewConnection firstConnection = getFirstConnection();

                hasGameStarted = true;
                lobby.newController();
                //add firstConnection player
                if(!initPlayerOfConnection(firstConnection)){
                    return;
                }
                numPlayers--;

                //add all BUT firstConnection player
                for (ViewConnection viewConnection : viewConnections) {
                    if(numPlayers>0){
                        if(viewConnection != firstConnection){
                            if(!initPlayerOfConnection(viewConnection)){
                                return;
                            }
                            numPlayers--;
                        }
                    }else if(viewConnection != firstConnection){
                        waitingConnectionRemove(viewConnection);
                        connections.remove(viewConnection);
                        viewConnection.send(new ConnectionMessage(ConnectionMessage.Type.DISCONNECTION_TOO_MANY_PLAYERS));
                        //clientCloseConnection(viewConnection);
                        //viewConnection.closeConnection();
                    }
                }
                lobby.startGame(isPersistencyAvailable);

//        }else{
//            System.out.println("Entro nel controllo nickname persistenza");
//        }
    }

    private boolean initPlayerOfConnection(ViewConnection connection){
        String playerName = waitingConnection.get(connection);
        Player player;
        if(isPersistencyAvailable){
            player = lobby.checkPersistencyPlayer(playerName);
            if(player == null){
                notifyPersistencyFail(connection);
                return false;
            }
//            else{
//                connection.asyncSend(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_STARTING, player));
//            }
        }else{
            player = lobby.addPlayingPlayer(playerName);
            if(player == null){
                throw new RuntimeException("Newly created player from lobby is null");
//                return false;
            }
        }
        initRemoteView(player, connection);
        connection.send(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_STARTING, player));
        return true;
    }

    private void initRemoteView(Player player, ViewConnection viewConnection){
        RemoteView remoteView = new RemoteView(player, viewConnection);
        lobby.addRemoteView(remoteView);
    }

    private void notifyPersistencyFail(ViewConnection connection){
        //emit persistency fail event
        //close all connections
        connection.send(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME_PERSISTENCY));
        //TEMP >
        clientCloseConnection(connection);
    }

    public void run(){
        hasGameStarted = false;
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                SocketViewConnection socketConnection = new SocketViewConnection(newSocket, this);
                socketConnection.createObjectStream();
                connections.add(socketConnection);
                executor.submit(socketConnection);
                initMessageClient(socketConnection);
            } catch(StreamCorruptedException e){
                System.out.print("Connection attempt from unknown service");
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

    public void initMessageClient(SocketViewConnection socketConnection){
        if(/*lobby.getNumPlayers()==0*/ lobby==null) {
            createNewLobby();
        }
        if(socketConnection == getFirstConnection()){
            socketConnection.send(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.STARTGAME, isPersistencyAvailable));
        }else {
            socketConnection.send(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.NICKNAME, isPersistencyAvailable));
        }
    }

    public void checkUpRegistration(String nickName, Integer numPlayers, boolean wantsPersistency, SocketViewConnection connection){
        if(lobby == null){
            System.out.println("NULL lobby!!!");
            connection.send(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.NULL_LOBBY));
            return;
        }
        if(waitingConnection.containsKey(connection)){
            System.out.println("Multiple login attempts from the same connection");
            return;
        }
        if(!hasGameStarted) {
            if (!lobby.validateNickname(nickName)) {
                connection.send(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME));
                return;
            }
            if (connection == getFirstConnection()) {
//                if(isPersistencyAvailable){
                    if(!isPersistencyAvailable || !wantsPersistency){
                        lobby.clearGame();
                        isPersistencyAvailable = false;
                        if (numPlayers == null || !lobby.setNumPlayers(numPlayers)) {
                            connection.send(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS));
                            return;
                        }
//                        if (!lobby.setNumPlayers(numPlayers)) {
//                            connection.asyncSend(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS));
//                            return;
//                        }
//                    }else{//isPersistency
                        //numPlayers set by the lobby
//                    }
                    }

            }
            lobby.addWaitingPlayer(nickName);
            waitingConnection.put(connection, nickName);

            if (waitingConnection.containsKey(getFirstConnection()) && waitingConnection.size() >= lobby.getNumPlayers()) {
                createNewGame();
            } else {
                //client has to wait
                connection.send(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT));
            }
        }else{
            connection.send(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.GAME_ALREADY_START));
        }

    }

    private ViewConnection getFirstConnection(){
        if(connections.size() == 0){
            return null;
        }
        return connections.get(0);
    }

    public synchronized boolean createNewLobby() {
        if(this.lobby==null){
            System.out.println("Opening a new Lobby");
            lobby = new Lobby("./game.ser"); //pass filename here
            this.isPersistencyAvailable = lobby.persistencyLoadGame();
            return true;
        }else{
            return false;
        }
    }


    public void removeFromGameConnectionList(ViewConnection c) {
        waitingConnectionRemove(c);
        connections.remove(c);
        System.out.println("waiting "+waitingConnection.size());
        System.out.println("waiting "+connections.size());
    }
}

