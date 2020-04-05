package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSerializerTest {
    Game game;

    @BeforeEach
    void initGame(){
        game = new Game();
    }

    @Test
    void writeGame(){
        //new Game()
    }
    @Test
    void readWriteGame() {
        GameSerializer serializer = new GameSerializer("./game.ser");
        serializer.writeGame(game);
        Game newGame = serializer.readGame();
        assertEquals(game, newGame);
    }

}
