package it.polimi.ingsw.model;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    static Game game;
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
        game.startGame(players, true);
    }


    @Test
    void checkIfGameIsSetCorrectlyWithCards()
    {
        for(int i=0; i<players.size(); i++) {
            assertEquals(2, players.get(i).getNumWorkers());
            assertEquals(null, players.get(i).getCard());

        }
        //assertNotEquals(null, game.getTurn());
        //create boolean game.isTurnSet()
        assertNotEquals(null, game.getCurrentPlayer());
        //Per Tia:
        //Non so se abbia senso fare il getter pubblico del player
        //Si potrebbe fare un metodo isSetCurrentPlayer()
        //o qualcosa di simile
        //Se no lasciamo perdere questi controlli

        assertEquals(0,game.getChosenCards().size());
        assertFalse(game.isSetFirstPlayer());
    }

    @Test
    void checkIfGameIsSetCorrectlyWithoutCards()
    {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        for(int i=0; i<players.size(); i++) {
            assertEquals(2, players.get(i).getNumWorkers());
            assertNotEquals(null, players.get(i).getCard());
            assertEquals("Default", players.get(i).getCard().getName());
        }
        //assertNotEquals(null, game.getTurn());
        //create boolean game.isTurnSet()
        assertNotEquals(null, game.getCurrentPlayer());
        assertEquals(0,game.getChosenCards().size());
        assertTrue(game.isSetFirstPlayer());
        assertTrue(game.getCurrentPlayer().isChallenger());
    }

    @Test
    void checkCorrectNumPlayers()
    {
        assertEquals(3, players.size());
    }

    @Test
    void checkCorrectNumPlayers2()
    {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        players.add(player0);
        players.add(player1);
        Game game = new Game();
        game.startGame(players, false);
        assertEquals(2, players.size());
    }

    @Test
    void checkIncorrectNum1Players()
    {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        players.add(player0);
        Game game = new Game();
        Exception exception = assertThrows(RuntimeException.class, () -> {
            game.startGame(players, false);
        });

        String expectedMessage = "Number of Players out of range";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkIncorrectNum4Players()
    {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("Player3");
        Player player3 = new Player("Player4");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        Game game = new Game();
        Exception exception = assertThrows(RuntimeException.class, () -> {
            game.startGame(players, false);
        });

        String expectedMessage = "Number of Players out of range";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void setChallengerChosenCards(){
        //TODO control that name of the string
        ArrayList<String> chosenCards = new ArrayList<>();
        chosenCards.add("Apollo");
        chosenCards.add("Artemis");
        chosenCards.add("Athena");
        game.setChosenCards(chosenCards);
        for(int i=0; i<chosenCards.size(); i++)
            assertEquals(game.getChosenCards().get(i).getName(), chosenCards.get(i));
        assertEquals(3, game.getChosenCards().size());
    }

    @Test
    void chooseCorrectlyACard(){
        ArrayList<String> chosenCards = new ArrayList<>();
        chosenCards.add("Apollo");
        chosenCards.add("Artemis");
        chosenCards.add("Athena");
        game.setChosenCards(chosenCards);
        Player currentTurnPlayer = game.getCurrentPlayer();
        game.setPlayerCards("Apollo");
        assertEquals("Apollo", currentTurnPlayer.getCard().getName());
        assertEquals(2, game.getChosenCards().size());
    }

    @Test
    void chooseCorrectlyTwoCard(){
        ArrayList<String> chosenCards = new ArrayList<>();
        chosenCards.add("Apollo");
        chosenCards.add("Artemis");
        chosenCards.add("Athena");
        game.setChosenCards(chosenCards);
        Player currentTurnPlayer = game.getCurrentPlayer();
        game.setPlayerCards("Apollo");
        assertEquals("Apollo", currentTurnPlayer.getCard().getName());
        assertEquals(2, game.getChosenCards().size());
        game.nextTurn();
        Player currentTurnPlayer2 = game.getCurrentPlayer();
        game.setPlayerCards("Artemis");
        assertEquals("Artemis", currentTurnPlayer2.getCard().getName());
        assertEquals(1, game.getChosenCards().size());

    }

    @Test
    void setFirstTurnCompletely(){
        ArrayList<String> chosenCards = new ArrayList<>();
        chosenCards.add("Apollo");
        chosenCards.add("Artemis");
        chosenCards.add("Athena");
        game.setChosenCards(chosenCards);
        game.nextTurn();
        Player currentTurnPlayer = game.getCurrentPlayer();
        game.setPlayerCards("Apollo");
        assertEquals("Apollo", currentTurnPlayer.getCard().getName());
        assertEquals(2, game.getChosenCards().size());
        game.nextTurn();
        Player currentTurnPlayer2 = game.getCurrentPlayer();
        game.setPlayerCards("Artemis");
        assertEquals("Artemis", currentTurnPlayer2.getCard().getName());
        assertEquals(1, game.getChosenCards().size());
        game.nextTurn();
        Player currentTurnPlayer3 = game.getCurrentPlayer();
        assertTrue(currentTurnPlayer3.isChallenger());
        game.setPlayerCards("Athena");
        assertEquals("Athena", currentTurnPlayer3.getCard().getName());
        assertEquals(0, game.getChosenCards().size());
        assertFalse(game.isSetFirstPlayer());
        game.firstTurn(currentTurnPlayer2);
        assertEquals(game.getCurrentPlayer(), currentTurnPlayer2);
        assertTrue(game.isSetFirstPlayer());

    }

    @Test
    void isAnyWorkerPlaced(){
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        assertTrue(game.isAnyWorkerNotPlaced());
    }

    @Test
    void isAnyWorkerNotPlaced() throws PositionOutOfBoundsException {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        Position workerPosition = new Position(1,1);
        Position workerPosition1 = new Position(2,2);
        game.place(workerPosition);
        game.place(workerPosition1);
        assertFalse(game.isAnyWorkerNotPlaced());
    }

    /**
     * Set a worker on the board
     * @throws PositionOutOfBoundsException
     */
    @Test
    void setWorkerOnTheBoard() throws PositionOutOfBoundsException {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        Position workerPosition = new Position (0,0);
        assertEquals(game.place(workerPosition),0);
        assertTrue(game.isAnyWorkerNotPlaced());
    }

    /**
     * Set a worker on the board in a occupied cell
     * @throws PositionOutOfBoundsException
     */
    @Test
    void setWorkerOnTheBoardInAOccupiedCell() throws PositionOutOfBoundsException {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        Position workerPosition = new Position (0,0);
        Position workerPosition2 = new Position (1,1);
        game.place(workerPosition);
        game.place(workerPosition2);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        assertTrue(game.isAnyWorkerNotPlaced());
        assertEquals(-1, game.place(workerPosition));
    }

    @Test
    void isFeasibleDefaultMove() throws PositionOutOfBoundsException {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        Position workerPosition1Player1 = new Position (0,0);
        Position workerPosition2Player1 = new Position (1,1);
        game.place(workerPosition1Player1);
        game.place(workerPosition2Player1);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position workerPosition1Player2 = new Position (4,4);
        Position workerPosition2Player2 = new Position (3,3);
        game.place(workerPosition1Player2);
        game.place(workerPosition2Player2);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position workerPosition1Player3 = new Position (4,0);
        Position workerPosition2Player3 = new Position (3,0);
        game.place(workerPosition1Player3);
        game.place(workerPosition2Player3);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position destPosition = new Position(0,1);
        assertTrue(game.isAllowedToMove());
        assertTrue(game.isRequiredToMove());
        game.isFeasibleMove(0,destPosition);
        //non esiste piu' updateCurrentWorker
        //game.updateCurrentWorker(0); //it is important update the currentWorker
        assertTrue(game.checkCurrentWorker(0));
        game.move(0,destPosition);
        assertEquals(destPosition,game.getCurrentPlayer().getWorkerCurrentPosition(0));
        assertFalse(game.isAllowedToMove());
        assertFalse(game.isRequiredToMove());
        assertFalse(game.checkCurrentWorker(1));

    }

    @Test
    void isNotFeasibleDefaultMove() throws PositionOutOfBoundsException {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        Position workerPosition1Player1 = new Position (0,0);
        Position workerPosition2Player1 = new Position (1,1);
        game.place(workerPosition1Player1);
        game.place(workerPosition2Player1);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position workerPosition1Player2 = new Position (4,4);
        Position workerPosition2Player2 = new Position (3,3);
        game.place(workerPosition1Player2);
        game.place(workerPosition2Player2);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position workerPosition1Player3 = new Position (4,0);
        Position workerPosition2Player3 = new Position (3,0);
        game.place(workerPosition1Player3);
        game.place(workerPosition2Player3);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position destPosition = new Position(0,0);
        assertTrue(game.isAllowedToMove());
        assertTrue(game.isRequiredToMove());
        assertFalse(game.isFeasibleMove(0,destPosition));
        //assertNotEquals(destPosition,game.getCurrentPlayer().getWorkerCurrentPosition(0));
        assertTrue(game.isAllowedToMove());
        assertTrue(game.isRequiredToMove());
        //assertFalse(game.checkCurrentWorker(1));

    }

    @Test
    void isFeasibleDefaultBuild() throws PositionOutOfBoundsException {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        Position workerPosition1Player1 = new Position (0,0);
        Position workerPosition2Player1 = new Position (1,1);
        game.place(workerPosition1Player1);
        game.place(workerPosition2Player1);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position workerPosition1Player2 = new Position (4,4);
        Position workerPosition2Player2 = new Position (3,3);
        game.place(workerPosition1Player2);
        game.place(workerPosition2Player2);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position workerPosition1Player3 = new Position (4,0);
        Position workerPosition2Player3 = new Position (3,0);
        game.place(workerPosition1Player3);
        game.place(workerPosition2Player3);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position destPosition = new Position(0,1);
        assertTrue(game.isAllowedToMove());
        assertTrue(game.isRequiredToMove());
        game.isFeasibleMove(0,destPosition);
       // game.updateCurrentWorker(0); //it is important update the currentWorker
        assertTrue(game.checkCurrentWorker(0));
        game.move(0,destPosition);
        assertTrue(game.isRequiredToBuild());
        assertTrue(game.isAllowedToBuild());
        Position destBuildPosition = new Position(0,0);
        assertTrue(game.isFeasibleBuild(0,destBuildPosition, false));

    }

    @Test
    void isNotFeasibleDefaultBuild() throws PositionOutOfBoundsException {
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        Position workerPosition1Player1 = new Position (0,0);
        Position workerPosition2Player1 = new Position (1,1);
        game.place(workerPosition1Player1);
        game.place(workerPosition2Player1);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position workerPosition1Player2 = new Position (4,4);
        Position workerPosition2Player2 = new Position (3,3);
        game.place(workerPosition1Player2);
        game.place(workerPosition2Player2);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position workerPosition1Player3 = new Position (4,0);
        Position workerPosition2Player3 = new Position (3,0);
        game.place(workerPosition1Player3);
        game.place(workerPosition2Player3);
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();
        Position destPosition = new Position(0,1);
        assertTrue(game.isAllowedToMove());
        assertTrue(game.isRequiredToMove());
        game.isFeasibleMove(0,destPosition);

        //non esiste piu'
        //game.updateCurrentWorker(0); //it is important update the currentWorker
        assertTrue(game.checkCurrentWorker(0));
        game.move(0,destPosition);
        Position destBuildPosition = new Position(0,0);
        assertFalse(game.isFeasibleBuild(0,destPosition, false));
        assertFalse(game.isFeasibleBuild(0,destPosition,true));

    }


}