package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameSerializerTest {
    Game game;
    Player player0;

    @BeforeEach
    void initGame(){
        game = new Game();
        ArrayList<Player> players = new ArrayList<>();
        player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        game.startGame(players, true);
    }

    @Test
    void writeGame0(){
        //game which has just started
        GameSerializer serializer = new GameSerializer("./game.ser");
        serializer.writeGame(game);
        Game newGame = serializer.readGame();
        assertEquals(game, newGame);
    }

    @Test
    void writeGame1(){
        //game in setCardTurn
        setCardTurn();
        GameSerializer serializer = new GameSerializer("./game.ser");
        serializer.writeGame(game);
        Game newGame = serializer.readGame();
        assertEquals(game, newGame);

    }

    @Test
    void writeGame2() throws Exception{
        //game in placeWorkersTurn
        setCardTurn();
        setPlaceWorkerTurn();
        GameSerializer serializer = new GameSerializer("./game.ser");
        serializer.writeGame(game);
        Game newGame = serializer.readGame();
        assertEquals(game, newGame);
    }


    private void setCardTurn(){
        ArrayList<String> chosenCards = new ArrayList<>();
        chosenCards.add("Apollo");
        chosenCards.add("Artemis");
        chosenCards.add("Athena");
        game.setChosenCards(chosenCards);
        game.nextTurn();

        game.setPlayerCard("Apollo");
        game.nextTurn();

        game.setPlayerCard("Artemis");
        game.nextTurn();

        game.setPlayerCard("Athena");
        //game.firstTurn(player0);
        game.firstTurn(player0);

    }

    private void setPlaceWorkerTurn() throws Exception {

        Position workerPosition1Player1 = new Position (0,0);
        Position workerPosition2Player1 = new Position (1,1);
        game.place(workerPosition1Player1);
        game.place(workerPosition2Player1);
        game.nextTurn();
        Position workerPosition1Player2 = new Position (4,4);
        Position workerPosition2Player2 = new Position (3,3);
        game.place(workerPosition1Player2);
        game.place(workerPosition2Player2);
        game.nextTurn();
        Position workerPosition1Player3 = new Position (4,0);
        Position workerPosition2Player3 = new Position (3,0);
        game.place(workerPosition1Player3);
        game.place(workerPosition2Player3);
        game.nextTurn();
    }


}
