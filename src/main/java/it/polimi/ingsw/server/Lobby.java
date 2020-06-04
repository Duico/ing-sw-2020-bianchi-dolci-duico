package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameViewEventListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.event.ModelEvent;
import it.polimi.ingsw.view.ControllerResponseListener;
import it.polimi.ingsw.view.ModelEventListener;
import it.polimi.ingsw.view.RemoteView;

import java.io.FileNotFoundException;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Lobby {
    String persistencyFilename = "./game.ser";
    //TODO should be in a configuration file
    private  Game game = null;
    private Controller controller;
    private Integer numPlayers;
    //only needed to check for duplicate nicknames
    private List<String> waitingPlayers;
    private List<Player> playingPlayers;
    private List<Player> persistencyPlayers;

    //ArrayList<Player> players;//fix ArrayList<String> -> ArrayList<Player>

    public Lobby() {
        //players = new ArrayList<Player>();
        waitingPlayers = new ArrayList<>();
    }
    public Lobby(String persistencyFilename){
        this();
        this.persistencyFilename = persistencyFilename;
    }

    /*public boolean validateNickname(String nickname){
        for(Player player: players){
            if(player.getNickName() == nickname)
                return false;
        }
        final Pattern pattern = Pattern.compile("^[A-Za-z0-9\\-_]{3,32}$");
        if (!pattern.matcher(nickname).matches()) {
            return false;
        }
        return true;
    }*/

    public boolean validateNickname(String nickname){
        for(String player: waitingPlayers){
            if(player.equals(nickname))
                return false;
        }
        final Pattern pattern = Pattern.compile("^[A-Za-z0-9\\-_]{3,32}$");
        if (!pattern.matcher(nickname).matches()) {
            return false;
        }
        return true;
    }

    public void addWaitingPlayer(String nickname) {
        waitingPlayers.add(nickname);
    }

    public Player checkPersistencyPlayer(String nickname){
        if(persistencyPlayers==null){
            System.out.print("Error with loading players from persistency.");
            return null;
        }
//        if(!validateNickname(nickname)){
//            return false;
//        }
        for(Player player : persistencyPlayers){
            if(player.getNickName().equals(nickname)){
                //to avoid multiple players taking the same name
                persistencyPlayers.remove(player);
                return player;
            }
        }
        return null;
//        Player player = new Player(nickname);
//        waitingPlayers.add(player);
//        return player;

    }

    public Player addPlayingPlayer(String nickname){
        Player newPlayer = new Player(nickname);
        playingPlayers.add(newPlayer);
        System.out.println("Add playing player: "+nickname);
        return newPlayer;
    }

    public void removePlayerByName(String nickname){
        waitingPlayers.removeIf(player -> player.equals(nickname));
    }


//    public boolean isAdmin(Player player){
//        if(this.players==null){
//            return false;
//        }
//        return this.players.get(0).equals(player);
//    }

    /*public void startGame() throws NotEnoughPlayersException {
        if(players.size() < getNumPlayers()) {
            throw new NotEnoughPlayersException();
        } else if(players.size() > getNumPlayers()){
            //ask to choose which players are going to play
        }
        game.startGame(players, true); //TODO when view pass useCards

    }*/

    /**
     * Read a serialized Game
     * @return Null if read Game is not valid, a Game if it is valid
     */
    public boolean persistencyLoadGame(){
        GameSerializer gameSerializer = new GameSerializer(persistencyFilename);
        Game readGame = null;
        try {
            readGame = gameSerializer.readGame();
        }catch (FileNotFoundException e) {
            System.out.println("No game save found on disk...");
            return false;
        } catch(InvalidClassException e) {
            System.out.println("Game save corrupted (Invalid Class)");
            return false;
        }
        if(!Game.validateGame(readGame)){
            return false;
        }
//        PersistencyEvent persistencyEvent = game.makePersistencyEvent();
//        if(!validatePlayersPersistency(persistencyEvent)){
//            return null;
//        }
        System.out.println("Game save found on disk: persistency available.");
        this.persistencyPlayers = readGame.getPlayers();
        setNumPlayers(persistencyPlayers.size());
//        String names = persistencyPlayers.stream().
//        System.out.println()
        this.game = readGame;
        game.clearEventListeners(ModelEventListener.class);
        return true;
    }

    public boolean persistencySaveGame(){
        if(this.game == null){
            return false;
        }
        TurnPhase turnPhase = this.game.getTurnPhase();
        if(!(turnPhase.equals(TurnPhase.PLACE_WORKERS) || turnPhase.equals(TurnPhase.NORMAL))){
            return false;
        }
        System.out.println("Saving game to disk...");
        GameSerializer gameSerializer = new GameSerializer(persistencyFilename);
        return gameSerializer.writeGame(game);
    }


    public Integer getNumPlayers(){
        //if(numPlayers>=2 && numPlayers<=3){
        return numPlayers;

    }

    public boolean setNumPlayers(Integer numPlayers) {
        if(numPlayers == null){
            return false;
        }
//        if(this.numPlayers!=null)
//            return false;
        if(2<=numPlayers && numPlayers<=3) {
            this.numPlayers = numPlayers;
            return true;
        }else
            return false;
    }


    public void newGame() {
        this.game = new Game();
        playingPlayers = new ArrayList<>();
    }

    public void newController(){
        if(game==null){
            newGame();
        }
        this.controller = new Controller(game);
    }

    public void startGame(boolean isPersistencyAvailable) {
        //                for (Player player : playingPlayers) {
//                    System.out.println(player.getNickName());
//                }
        if(isPersistencyAvailable){
            System.out.print("Resuming game.");
            //no need for playingPlayers here
            game.resumeGame();
        }else {
            System.out.println("Starting new game");

            game.startGame(playingPlayers, true);
        }
    }

    public void clearGame() {
        this.game = null;
        persistencyPlayers = null;
    }

    public boolean isSetGame(){
        return game!=null;
    }

    public void addRemoteView(RemoteView remoteView) {
//        playingPlayers.add(player);
        remoteView.addEventListener(GameViewEventListener.class, controller);
        controller.addEventListener(ControllerResponseListener.class, remoteView);
//        remoteViews.add(remoteView);
        game.addEventListener(ModelEventListener.class, remoteView);
    }

}
