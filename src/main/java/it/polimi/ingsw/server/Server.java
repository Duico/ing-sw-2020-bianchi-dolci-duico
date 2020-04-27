package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
//import it.polimi.ingsw.model.event.PersistencyEvent;

public class Server {
    protected void init(){
        Lobby lobby = new Lobby("./game.ser"); //pass filename here
        Game loadedGame = lobby.persistencyLoadGame();
        if(loadedGame != null){
            //PersistencyEvent p = new PersistencyEvent(loadedGame);
            //send p to client via socket
        }else{
            //create and start new game
        }

    }
}
