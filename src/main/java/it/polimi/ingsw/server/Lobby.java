package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameViewEventListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSerializer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.view.ControllerResponseListener;
import it.polimi.ingsw.view.ModelEventListener;
import it.polimi.ingsw.view.RemoteView;

import java.io.FileNotFoundException;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * class that manages login of players, creation of the lobby and start the game
 * allows to reload a game that was interrupted by reading a configuration file where game state has been saved
 */
public class Lobby {
    String persistencyFilename = "./game.ser";

    private Game game = null;
    private Controller controller;
    private Integer numPlayers;
    //only needed to check for duplicate nicknames
    private List<String> waitingPlayers;
    private List<Player> playingPlayers;
    private List<Player> persistencyPlayers;


    public Lobby() {
        waitingPlayers = new ArrayList<>();
    }
    public Lobby(String persistencyFilename){
        this();
        this.persistencyFilename = persistencyFilename;
    }


    /**
     * checks if entered name is an allowed nickname
     * @param nickname name entered by the player
     * @return true if name entered is ok
     */
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

    /**
     * checks if player's nickname is in the saved game's players list
     * @param nickname nickname of the player who is reloading the game
     * @return the player if was in the saved game's players list
     */
    public Player checkPersistencyPlayer(String nickname){
        if(persistencyPlayers==null){
            System.out.print("Error with loading players from persistency.");
            return null;
        }

        for(Player player : persistencyPlayers){
            if(player.getNickName().equals(nickname)){
                //to avoid multiple players taking the same name
                persistencyPlayers.remove(player);
                return player;
            }
        }
        return null;
    }

    /**
     * add a new player in the game
     * @param nickname the nickname of the player
     * @return player
     */
    public Player addPlayingPlayer(String nickname){
        Player newPlayer = new Player(nickname);
        playingPlayers.add(newPlayer);
        System.out.println("Add playing player: "+nickname);
        return newPlayer;
    }

    /**
     * remove a player from the waitingPlayers list
     * @param nickname the nickname of the player
     */
    public void removePlayerByName(String nickname){
        waitingPlayers.removeIf(player -> player.equals(nickname));
    }

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

        System.out.println("Game save found on disk: persistency available.");
        this.persistencyPlayers = readGame.getPlayers();
        setNumPlayers(persistencyPlayers.size());

        this.game = readGame;
        game.clearEventListeners(ModelEventListener.class);
        return true;
    }

    /**
     * checks if game can be saved and eventually writes it on persistency file
     * @return true if game is successfully saved
     */
    public boolean persistencySaveGame(){
        if(this.game == null){
            return false;
        }
        if(!this.game.isActive()){
            System.out.println("Game is over. Skipping save to disk.");
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
        return numPlayers;

    }

    /**
     * sets number of players of the lobby checking if the number is 2 or 3
     * @param numPlayers number of players who enters the lobby
     * @return true if number of players is successfully set
     */
    public boolean setNumPlayers(Integer numPlayers) {
        if(numPlayers == null){
            return false;
        }

        if(2<=numPlayers && numPlayers<=3) {
            this.numPlayers = numPlayers;
            return true;
        }else
            return false;
    }


    /**
     * Create a new game and a list of players
     */
    public void newGame() {
        this.game = new Game();
        playingPlayers = new ArrayList<>();
    }

    /**
     * create a new controller for the game
     */
    public void newController(){
        if(game==null){
            newGame();
        }
        this.controller = new Controller(game);
    }

    /**
     * starts a game checking if has been interrupted before and eventually reload it from persistency file
     * @param isPersistencyAvailable true if the game was interrupted before and can be reloaded
     */
    public void startGame(boolean isPersistencyAvailable) {

        if(isPersistencyAvailable){
            System.out.print("Resuming game.");
            //no need for playingPlayers here
            game.resumeGame();
        }else {
            System.out.println("Starting new game");

            game.startGame(playingPlayers, true);
        }
    }

    /**
     * set the game and the persistencyPlayers to null
     */
    public void clearGame() {
        this.game = null;
        persistencyPlayers = null;
    }

    public boolean isSetGame(){
        return game!=null;
    }

    /**
     * add event listeners to a remoteView
     * @param remoteView remote view of a single player
     */
    public void addRemoteView(RemoteView remoteView) {
        remoteView.addEventListener(GameViewEventListener.class, controller);
        controller.addEventListener(ControllerResponseListener.class, remoteView);
        game.addEventListener(ModelEventListener.class, remoteView);
    }

}
