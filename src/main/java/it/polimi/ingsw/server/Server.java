
package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameViewEventListener;
import it.polimi.ingsw.server.message.DisconnectionSetUpMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSerializer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.message.SetUpType;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ModelEventListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import it.polimi.ingsw.model.event.PersistencyEvent;

public class Server {

    private static final int PORT = 12345;
    private Lobby lobby;
    private Game game;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(100);
    private Map<ViewConnection,Player> waitingConnection = new LinkedHashMap<>();
    private ArrayList<ViewConnection> connections = new ArrayList<>();
    private boolean hasGameStarted = false;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        init();
    }

    public synchronized void clientCloseConnection(ViewConnection c) {
        waitingConnection.remove(c);
        boolean isFirstConnection = c == getFirstConnection();
        connections.remove(c);
        if(hasGameStarted || isFirstConnection) {
            //ArrayList<ViewConnection> viewConnections = new ArrayList<>(waitingConnection.keySet());
            for (ViewConnection connection : connections) {
                connection.asyncSend(new DisconnectionSetUpMessage(SetUpType.DISCONNECTION));
            }
            connections.clear();
            waitingConnection.clear();
            GameSerializer serializer = new GameSerializer("./game.ser");
            serializer.writeGame(game);
            this.game = null;
            this.lobby = null;
        }else{
            //client disconnected from lobby
        }

    }


    public synchronized Player putConnection(ViewConnection c, String name){
        Player newPlayer = new Player(name);
        waitingConnection.put(c, newPlayer);
        return newPlayer;
    }

    public synchronized void createNewGame(){
            if(game==null) {
            //if (waitingConnection.size() == lobby.getNumPlayers()) {
                Integer numPlayers = lobby.getNumPlayers();

                ArrayList<ViewConnection> viewConnections = new ArrayList<>(waitingConnection.keySet());
                ArrayList<Player> players = new ArrayList<>();
                ArrayList<RemoteView> remoteViews = new ArrayList<>();
                ViewConnection firstConnection = getFirstConnection();

                hasGameStarted = true;
                game = new Game();
                Controller controller = new Controller(game);

                //add firstConnection player
                initPlayer(firstConnection, controller, players, remoteViews);
                numPlayers--;

                //add all BUT firstConnection player
                for (ViewConnection viewConnection : viewConnections) {
                    if(numPlayers>0){
                        if(viewConnection != firstConnection){
                            initPlayer(viewConnection, controller, players, remoteViews);
                            numPlayers--;
                        }
                    }else{
                        viewConnection.asyncSend(new DisconnectionSetUpMessage(SetUpType.TOO_MANY_PLAYERS));
                        //viewConnection.closeConnection();
                    }
                }
                for (Player player : players) {
                    System.out.println(player.getNickName());
                }
                game.startGame(players, true);


           // }
        }else{
            System.out.println("Entro nel controllo nickname persistenza");
        }


    }

    private void initPlayer(ViewConnection viewConnection, Controller controller, ArrayList<Player> players, ArrayList<RemoteView> remoteViews){
        Player player = waitingConnection.get(viewConnection);
        players.add(player);
        RemoteView remoteView = new RemoteView(player, viewConnection);
        remoteView.addEventListener(GameViewEventListener.class, controller);
        remoteViews.add(remoteView);
        game.addEventListener(ModelEventListener.class, remoteView);
    }

    public void run(){
        //TODO ??needed??
        hasGameStarted = false;
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                SocketViewConnection socketConnection = new SocketViewConnection(newSocket, this);
                connections.add(socketConnection);
                socketConnection.createObjectStream();
                executor.submit(socketConnection);
                initMessageClient(socketConnection);

            } catch (IOException e) {
                System.out.println("Connection Error!");
                System.out.println("Il server ha perso la connessione!");
            }
        }
    }

    public void initMessageClient(SocketViewConnection socketConnection){
        if(/*lobby.getNumPlayers()==0*/ lobby==null) {
            createNewLobby();
            socketConnection.asyncSend(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.STARTGAME));
        }else {
            socketConnection.asyncSend(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.NICKNAME));
        }
    }

    public void checkUpRegistration(String nickName, Integer numPlayers, SocketViewConnection connection){
            if(!lobby.addPlayer(nickName)){
                connection.asyncSend(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME));
                return;
            }
            if(connection == getFirstConnection()){
                 if(numPlayers == null){
                    connection.asyncSend(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS));
                    return;
                 }
                if(!lobby.setNumPlayers(numPlayers)) {
                    connection.asyncSend(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS));
                    return;
                }

            }
            Player newPlayer = putConnection(connection, nickName);
            connection.asyncSend(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP, newPlayer));

            if(lobby.getNumPlayers() != null && waitingConnection.size() >= lobby.getNumPlayers()) {
                createNewGame();
            }

//        } else if (lobby.addPlayer(nickName)) {
//            connection.asyncSend(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP));
//            this.lobby(connection, nickName);
//        } else{
//            connection.asyncSend(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME));
//        }
    }

    private ViewConnection getFirstConnection(){
        if(connections.size() == 0){
            return null;
        }
        return connections.get(0);
    }

    /*public int getNumPlayers() {
        return lobby.getNumPlayers();
    }*/

//    public synchronized boolean setNumPlayers(int numPlayers) {
//        return lobby.setNumPlayers(numPlayers);
//    }


    public synchronized boolean nameAlreadyUsed(String name) {
        return lobby.validateNickname(name);
    }

    public synchronized boolean addPlayer(String nickName){
        if(lobby.getNumPlayers()==0){
            lobby.addPlayer(nickName);

        }else{
            if(!nameAlreadyUsed(nickName))
                return false;
            lobby.addPlayer(nickName);
        }
        return true;
    }


    public synchronized boolean createNewLobby() {
        if(this.lobby==null){
            this.lobby = new Lobby();
            return true;
        }else{
            return false;
        }
    }

//    public void addWaitingGame(ViewConnection c){
//        waitingGameConnection.add(c);
//    }


    protected void init(){
        /*lobby = new Lobby("./game.ser"); //pass filename here
        Game loadedGame = lobby.persistencyLoadGame();
        if(loadedGame != null){
            game=loadedGame;
            System.out.println("return to game serialized");
            //PersistencyEvent p = new PersistencyEvent(loadedGame);
            //send p to client via socket
        }else{
            createNewGame();
            System.out.println("new Game");
            //create and start new game
        }*/
        //createNewGame();
    }


}

