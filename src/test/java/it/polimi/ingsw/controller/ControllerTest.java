package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ViewConnection;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.event.ChallengerCardViewEvent;
import it.polimi.ingsw.view.event.EndTurnViewEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {


        static Game game;
        static Controller controller;
        static ArrayList<Player> players;
        static Player player0;
        static Player player1;
        static Player player2;

        @BeforeEach
        public void setUp(){
            players = new ArrayList<>();
            player0 = new Player("Player1");
            player1 = new Player("Player2");
            player2 = new Player("player3");
            players.add(player0);
            players.add(player1);
            players.add(player2);
            game = new Game();
            controller = new Controller(game);
            game.startGame(players, true);

        }

        @Test
        void checkChallengerCards(){
            ArrayList<String> chosenCards = new ArrayList<>();
            chosenCards.add("Apollo");
            chosenCards.add("Artemis");
            chosenCards.add("Athena");
            Player currentPlayer = game.getCurrentPlayer();
            controller.challengerCards(new ChallengerCardViewEvent(currentPlayer, chosenCards));
            assertNotEquals(currentPlayer, game.getCurrentPlayer());

        }

}