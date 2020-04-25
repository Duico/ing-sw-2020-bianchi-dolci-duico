package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotEnoughPlayersException;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Lobby {
    String persistencyFilename = "./game.ser";
    //TODO should be in a configuration file
    Game game;
    int numPlayers;
    ArrayList<Player> players;//fix ArrayList<String> -> ArrayList<Player>

    public Lobby() {
        players = new ArrayList<Player>();
        this.game = new Game();
    }
    public Lobby(String persistencyFilename){
        this();
        this.persistencyFilename = persistencyFilename;
    }

    public void changeBoardSize(int width, int height){
        this.game = new Game(width, height,2);
    }

    public boolean validateNickname(String nickname){
        for(Player player: players){
            if(player.getNickName() == nickname)
                return false;
        }
        final Pattern pattern = Pattern.compile("^[A-Za-z0-9\\-_]{3,32}$");
        if (!pattern.matcher(nickname).matches()) {
            return false;
        }
        return true;
    }

    public Player addPlayer(String nickname){
        if(!validateNickname(nickname)){
            //todo
            //notify view
            return null;
        }
        Player newPlayer = new Player(nickname);
        players.add(newPlayer);
        return newPlayer;

    }

    public void removePlayerName(String nickname){
        for(Player player: players) {
            if (player.getNickName().equals(nickname)) {
                players.remove(player);
            }
        }
    }
    public boolean isAdmin(Player player){
        if(this.players==null){
            return false;
        }
        return this.players.get(0).equals(player);
    }

    public void startGame() throws NotEnoughPlayersException {
        if(players.size() < getNumPlayers()) {
            throw new NotEnoughPlayersException();
        } else if(players.size() > getNumPlayers()){
            //ask to choose which players are going to play
        }
        game.startGame(players, true); //TODO when view pass useCards

    }

    /**
     * Read a serialized Game
     * @return Null if read Game is not valid, a Game if it is valid
     */
    public Game persistencyLoadGame(){
        GameSerializer gameSerializer = new GameSerializer(persistencyFilename);
        Game readGame = gameSerializer.readGame();
        if(Game.validateGame(readGame)){
            return readGame;
        }else{
            return null;
        }
    }

    public boolean persistencySaveGame(){
        GameSerializer gameSerializer = new GameSerializer(persistencyFilename);
        return gameSerializer.writeGame(this.game);
    }


    public int getNumPlayers(){
        //if(numPlayers>=2 && numPlayers<=3){
            return numPlayers;

    }
}
