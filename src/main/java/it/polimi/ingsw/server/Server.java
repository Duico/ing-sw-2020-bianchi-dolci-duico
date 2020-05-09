
package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameViewEventListener;
import it.polimi.ingsw.message.ErrorMessage;
import it.polimi.ingsw.message.ErrorTyper;
import it.polimi.ingsw.message.ServerLobbyResponse;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSerializer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ModelEventListener;
import it.polimi.ingsw.message.*;
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
    private Map<ViewConnection,Player> waitingConnection = new HashMap<>();
    private ArrayList<ViewConnection> waitingGameConnection = new ArrayList<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        init();
    }

    public synchronized void clientCloseConnection(ViewConnection c) {
        waitingConnection.remove(c);
        ArrayList<ViewConnection> viewConnections = new ArrayList<>(waitingConnection.keySet());
        for (int i = 0; i < viewConnections.size(); i++){
            viewConnections.get(i).asyncSend(new ErrorMessage(ErrorTyper.DISCONNECT, "La partita è terminata causa disconnessione di un player"));

        }
        waitingConnection.clear();
        GameSerializer serializer = new GameSerializer("./game.ser");
        serializer.writeGame(game);

    }

    public void adviseAllAttendances(){
        for(int i=0;i<waitingGameConnection.size();i++)
            waitingGameConnection.get(i).asyncSend("Welcome, add your nickName");
    }


    public synchronized void lobby(ViewConnection c, String name){
            if(game==null) {
            waitingConnection.put(c, new Player(name));
            if (waitingConnection.size() == lobby.getNumPlayers()) {

                ArrayList<ViewConnection> viewConnections = new ArrayList<>(waitingConnection.keySet());
                ArrayList<Player> players = new ArrayList<>();

                for (int i = 0; i < waitingConnection.size(); i++) {
                    players.add(waitingConnection.get(viewConnections.get(i)));
                }


                ArrayList<RemoteView> remoteView = new ArrayList<>();
                for (int i = 0; i < players.size(); i++) {
                    remoteView.add(new RemoteView(players.get(i), viewConnections.get(i)));
                    viewConnections.get(i).asyncSend("La partita può incominciare");
                }

                game = new Game();
                Controller controller = new Controller(game);
                for (int i = 0; i < remoteView.size(); i++) {
                    remoteView.get(i).addEventListener(GameViewEventListener.class, controller);
                    game.addEventListener(ModelEventListener.class, remoteView.get(i));
                }
                game.startGame(players, true);


            }
        }else{
            System.out.println("Entro nel controllo nickname persistenza");
        }


    }


    public void run(){
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                SocketViewConnection socketConnection = new SocketViewConnection(newSocket, this);
                executor.submit(socketConnection);
                socketConnection.createObjectStream();
                initMessageClient(socketConnection);

            } catch (IOException e) {
                System.out.println("Connection Error!");
                System.out.println("Il server ha perso la connessione!");
            }
        }
    }

    public void initMessageClient(SocketViewConnection socketConnection){
        if(lobby.getNumPlayers()==0)
            socketConnection.asyncSend(new ServerLobbyResponse(ServerLobbyResponse.SingUpParameter.STARTGAME));
        else
            socketConnection.asyncSend(new ServerLobbyResponse(ServerLobbyResponse.SingUpParameter.NICKNAME));
    }

    public void checkUpRegistration(String nickName, int numPlayers, SocketViewConnection connection){
        if(lobby.getNumPlayers()==0){
            lobby.addPlayer(nickName);
            if(!lobby.setNumPlayers(numPlayers)) {
                connection.asyncSend(new FailedSignUpMessage(FailedSignUpMessage.Reason.INVALID_NUMPLAYERS));
                return;
            }
            this.lobby(connection, nickName);
            connection.asyncSend(new ServerLobbyResponse(ServerLobbyResponse.SingUpParameter.CORRECT_SIGNUP));

        } else if (lobby.addPlayer(nickName)) {
            connection.asyncSend(new ServerLobbyResponse(ServerLobbyResponse.SingUpParameter.CORRECT_SIGNUP));
            this.lobby(connection, nickName);
        } else{
            connection.asyncSend(new FailedSignUpMessage(FailedSignUpMessage.Reason.NICKNAME_ALREADY_USED));
        }
    }


    /*public int getNumPlayers() {
        return lobby.getNumPlayers();
    }*/

    public synchronized boolean setNumPlayers(int numPlayers) {
        return lobby.setNumPlayers(numPlayers);
    }


    public synchronized boolean nameAlreadyUsed(String name) {
        return lobby.validateNickname(name);
    }

    public synchronized boolean addPlayer(String nickName){
        if(lobby.getNumPlayers()==0){
            lobby.addPlayer(nickName);
            //lobby.setNumPlayers(3);
        }else{
            if(!nameAlreadyUsed(nickName))
                return false;
            lobby.addPlayer(nickName);
        }
        return true;
    }


    public synchronized boolean createNewGame() {
        if(this.lobby==null){
            this.lobby = new Lobby();

            return true;
        }else{
            return false;
        }
    }

    public void addWaitingGame(ViewConnection c){
        waitingGameConnection.add(c);
    }


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
        createNewGame();
    }


}

