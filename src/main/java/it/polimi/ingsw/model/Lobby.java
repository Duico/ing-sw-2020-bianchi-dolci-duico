package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.NotEnoughPlayersException;

import java.util.ArrayList;

public class Lobby {
    String persistencyFilename = "./game.ser";
    Game game;
    ArrayList<Player> playerNames;//fix ArrayList<String> -> ArrayList<Player>

    public Lobby() {
        playerNames = new ArrayList<Player>();
        this.game = new Game();
    }

    public void changeBoardSize(int width, int height){
        this.game = new Game(width, height,2);
    }

    public boolean addPlayerName(String nickname){//fix no nickname anymore
        if( !playerNames.contains(nickname) ){
            playerNames.add(nickname);
            return true;
        }else{
            return false;
        }

    }

    public void removePlayerName(String nickname){
        playerNames.remove(nickname);
    }

    public void startGame() throws NotEnoughPlayersException {
        if(playerNames.size() < 2) {
            throw new NotEnoughPlayersException();
        } else if(playerNames.size() > 3){
            //ask to choose which players are going to play
        }
        game.startGame(playerNames, true);

    }

    public void persistencyLoadGame(){
        GameSerializer gameSerializer = new GameSerializer(persistencyFilename);
        this.game = gameSerializer.readGame();
    }
    public void persistencySaveGame(){
        GameSerializer gameSerializer = new GameSerializer(persistencyFilename);
        gameSerializer.writeGame(this.game);
    }
}
