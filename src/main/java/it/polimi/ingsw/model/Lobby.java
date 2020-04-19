package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotEnoughPlayersException;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Lobby {
    String persistencyFilename = "./game.ser";
    Game game;
    int numPlayers;
    ArrayList<Player> players;//fix ArrayList<String> -> ArrayList<Player>

    public Lobby() {
        players = new ArrayList<Player>();
        this.game = new Game();
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

    public void startGame() throws NotEnoughPlayersException {
        if(players.size() < getNumPlayers()) {
            throw new NotEnoughPlayersException();
        } else if(players.size() > getNumPlayers()){
            //ask to choose which players are going to play
        }
        game.startGame(players, true); //TODO when view pass useCards

    }

    public boolean persistencyLoadGame(){
        GameSerializer gameSerializer = new GameSerializer(persistencyFilename);
        Game readGame = gameSerializer.readGame();
        if(Game.validateGame(readGame)){
            this.game = readGame;
            return true;
        }else{
            return false;
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
