package it.polimi.ingsw.model;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void checkIfPlayersAdded()
    {
        ArrayList<Player> nickNames = new ArrayList<>();
        nickNames.add("Player1");
        Game partita= new Game();
        partita.startGame(nickNames,true);
       assertTrue(partita.getPlayer(0).getNickName().equals("Player1"));
    }

    @Test
    void checkNumPlayers()
    {
        ArrayList<Player> nickNames = new ArrayList<>();
        nickNames.add("Player1");
        nickNames.add("Player2");
        nickNames.add("Player3");
        Game partita= new Game();
        partita.startGame(nickNames,true);
        assertTrue(partita.getPlayers().size()==3);

    }

}