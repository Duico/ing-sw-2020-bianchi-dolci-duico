
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

    /**
     * closes a client connection, removing it from list of connections and from waiting list
     * @param c view connection of the client
     */
    public synchronized void clientCloseConnection(ViewConnection c) {
        boolean isFirstConnection = c == getFirstConnection();
        if (isFirstConnection || (waitingConnection.containsKey(c) && hasGameStarted)) {
            waitingConnectionRemove(c);
            connections.remove(c);
            for (ViewConnection connection : connections) {
                connection.send(new ConnectionMessage(ConnectionMessage.Type.DISCONNECTION));
            }
            connections.clear();
            waitingConnection.clear();
            lobby.persistencySaveGame();
            lobby.clearGame();
            this.lobby = null;
            System.out.println("game, lobby set to null");
            hasGameStarted = false;
        } else {

            waitingConnectionRemove(c);
            connections.remove(c);
        }


    }

    private synchronized void waitingConnectionRemove(ViewConnection c){
        lobby.removePlayerByName(waitingConnection.get(c));
        waitingConnection.remove(c);
    }

    /**
     * starts game for all players connected to server and in the waiting list
     */
    public synchronized void createNewGame(){

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
            }
        }
        lobby.startGame(isPersistencyAvailable);

    }

    /**
     * picks player from waiting connection list and sets up his remote view
     * @param connection player view connection
     * @return true if player is successfully signed up
     */
    private boolean initPlayerOfConnection(ViewConnection connection){
        String playerName = waitingConnection.get(connection);
        Player player;
        if(isPersistencyAvailable){
            player = lobby.checkPersistencyPlayer(playerName);
            if(player == null){
                notifyPersistencyFail(connection);
                return false;
            }
        }else{
            player = lobby.addPlayingPlayer(playerName);
            if(player == null){
                throw new RuntimeException("Newly created player from lobby is null");
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

    /**
     * closes connection to the client
     * @param connection view connection of the client
     */
    private void notifyPersistencyFail(ViewConnection connection){
        connection.send(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME_PERSISTENCY));
        clientCloseConnection(connection);
    }

    /**
     * creates sockets to allow client connections
     */
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
                System.out.println("Connection attempt from unknown service");
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

    /**
     * sends set up message to client
     * @param socketConnection socket connection of client
     */
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

    /**
     * manages player's sign up checking entered nickname and if persistency is available
     * @param nickName nickname of the player
     * @param numPlayers number of players in the lobby
     * @param wantsPersistency true if the player is trying to load a game from disk
     * @param connection socket connection of the client
     */
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

                if(!isPersistencyAvailable || !wantsPersistency){
                    lobby.clearGame();
                    isPersistencyAvailable = false;
                    if (numPlayers == null || !lobby.setNumPlayers(numPlayers)) {
                        connection.send(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS));
                        return;
                    }

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

    /**
     * creates new lobby and eventually loads game from disk
     * @return true if lobby is successfully created
     */
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

    /**
     * removes client's connection from waiting list and list of connections
     * @param c view connection related to a client
     */
    public void removeFromGameConnectionList(ViewConnection c) {
        waitingConnectionRemove(c);
        connections.remove(c);
    }
}

