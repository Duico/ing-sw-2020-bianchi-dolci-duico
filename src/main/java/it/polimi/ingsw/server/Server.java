
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
            viewConnections.get(i).asyncSend(new DisconnectionSetUpMessage(SetUpType.DISCONNECTION));
        }
        waitingConnection.clear();
        GameSerializer serializer = new GameSerializer("./game.ser");
        serializer.writeGame(game);
        this.game=null;
        this.lobby=null;


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
            createNewGame();
            socketConnection.asyncSend(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.STARTGAME));
        }else {
            socketConnection.asyncSend(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.NICKNAME));
        }
    }

    public void checkUpRegistration(String nickName, Integer numPlayers, SocketViewConnection connection){
        if(lobby.getNumPlayers()==null){
            lobby.addPlayer(nickName);
            //if(firstConnetion connection)
            // numPlayers == null ERROR
            //
//            if(!lobby.setNumPlayers(numPlayers)) {
//                connection.asyncSend(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS));
//                return;
//            }
//            else()
            {

            this.lobby(connection, nickName);
            connection.asyncSend(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP));

            }

        } else if (lobby.addPlayer(nickName)) {
            connection.asyncSend(new InitSetUpMessage(SetUpType.SIGN_UP, InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP));
            this.lobby(connection, nickName);
        } else{
            connection.asyncSend(new SignUpFailedSetUpMessage(SetUpType.SIGN_UP, SignUpFailedSetUpMessage.Reason.NICKNAME_ALREADY_USED));
        }
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
        //createNewGame();
    }


}

