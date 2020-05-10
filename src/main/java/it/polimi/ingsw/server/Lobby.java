package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameSerializer;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exception.NotEnoughPlayersException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Lobby {
    String persistencyFilename = "./game.ser";
    //TODO should be in a configuration file
    Game game;
    Integer numPlayers;
    private ArrayList<String> players;

    //ArrayList<Player> players;//fix ArrayList<String> -> ArrayList<Player>

    public Lobby() {
        //players = new ArrayList<Player>();
        players = new ArrayList<>();
        this.game = new Game();
    }
    public Lobby(String persistencyFilename){
        this();
        this.persistencyFilename = persistencyFilename;
    }

    public void changeBoardSize(int width, int height){
        this.game = new Game(width, height,2);
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
        for(String player: players){
            if(player.equals(nickname))
                return false;
        }
        final Pattern pattern = Pattern.compile("^[A-Za-z0-9\\-_]{3,32}$");
        if (!pattern.matcher(nickname).matches()) {
            return false;
        }
        return true;
    }

    public boolean addPlayer(String nickname){
        if(!validateNickname(nickname)){
            return false;
        }

        players.add(nickname);
        return true;

    }

    /*public void removePlayerName(String nickname){
        for(Player player: players) {
            if (player.getNickName().equals(nickname)) {
                players.remove(player);
            }
        }
    }*/

    public void removePlayerName(String nickname){
        for(String player: players) {
            if (player.equals(nickname)) {
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


    public Integer getNumPlayers(){
        //if(numPlayers>=2 && numPlayers<=3){
            return numPlayers;

    }

    public boolean setNumPlayers(@NotNull Integer numPlayers) {
        if(this.numPlayers!=null)
            return false;
        else if(2<=numPlayers && numPlayers<=3) {
            this.numPlayers = numPlayers;
            return true;
        }else
            return false;
    }
}
