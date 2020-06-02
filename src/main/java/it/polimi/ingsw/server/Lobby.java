package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameViewEventListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSerializer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ModelEventListener;
import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Lobby {
    String persistencyFilename = "./game.ser";
    //TODO should be in a configuration file
    private  Game game = null;
    private Controller controller;
    Integer numPlayers;
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

//        if(!validateNickname(nickname)){
//            return false;
//        }
        for(Player player : persistencyPlayers){
            if(player.getNickName().equals(nickname)){
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
        System.out.print("Add playing player: "+nickname);
        return newPlayer;
    }

    public void removePlayerByName(String nickname){
        for(String player: waitingPlayers) {
            if (player.equals(nickname)) {
                waitingPlayers.remove(player);
            }
        }
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
        Game readGame = gameSerializer.readGame();
        if(!Game.validateGame(readGame)){
            this.game =  null;
            return false;
        }
//        PersistencyEvent persistencyEvent = game.makePersistencyEvent();
//        if(!validatePlayersPersistency(persistencyEvent)){
//            return null;
//        }
        this.persistencyPlayers = readGame.getPlayers();
        this.game = readGame;
        return true;
    }

    public boolean persistencySaveGame(){
        if(this.game == null){
            return false;
        }
        System.out.println("Saving game to disk...");
        GameSerializer gameSerializer = new GameSerializer(persistencyFilename);
        return gameSerializer.writeGame(game);
    }


    public Integer getNumPlayers(){
        //if(numPlayers>=2 && numPlayers<=3){
        if(persistencyPlayers!=null){
            return persistencyPlayers.size();
        }
        return numPlayers;

    }

    public boolean setNumPlayers(Integer numPlayers) {
        if(numPlayers == null){
            return false;
        }
        if(this.numPlayers!=null)
            return false;
        else if(2<=numPlayers && numPlayers<=3) {
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
            game.resumeGame();
        }else {
            System.out.print("Starting new game");
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
//        remoteViews.add(remoteView);
        game.addEventListener(ModelEventListener.class, remoteView);
    }
}
