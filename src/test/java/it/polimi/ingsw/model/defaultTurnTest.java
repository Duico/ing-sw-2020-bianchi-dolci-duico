package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class defaultTurnTest {
    ArrayList<Player> players;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("Player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
    }
    @Test
    void isAllowedToMove(){

    }
}
