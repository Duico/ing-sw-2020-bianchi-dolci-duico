package it.polimi.ingsw.model;
import it.polimi.ingsw.model.exception.IllegalTurnPhaseException;
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

    private Game setGameWithoutCards(){
        players = new ArrayList<>();
        Player player0 = new Player("Player1");
        Player player1 = new Player("Player2");
        Player player2 = new Player("player3");
        players.add(player0);
        players.add(player1);
        players.add(player2);
        Game game = new Game();
        game.startGame(players, false);
        return game;
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

    private void setPlaceWorkerTurn(Game game) throws PositionOutOfBoundsException {

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


    @Test
    void checkIfGameIsSetCorrectlyWithCards()
    {
        for(int i=0; i<players.size(); i++) {
            assertEquals(2, players.get(i).getNumWorkers());
            assertEquals(null, players.get(i).getCard());
        }
        assertEquals(0,game.getChosenCards().size());
        assertFalse(game.isSetFirstPlayer());
        assertEquals(TurnPhase.CHOSE_CARDS, game.getTurnPhase());
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

        assertTrue(game.isSetFirstPlayer());
        assertEquals(TurnPhase.PLACE_WORKERS, game.getTurnPhase());

    }

    @Test
    void checkCorrectTurnPhase() throws PositionOutOfBoundsException {
        Game game=setGameWithoutCards();
        assertEquals(TurnPhase.PLACE_WORKERS, game.getTurnPhase());
        setPlaceWorkerTurn(game);
        assertEquals(TurnPhase.NORMAL, game.getTurnPhase());
    }

    @Test
    void checkCorrectTurnPhase2() throws PositionOutOfBoundsException {
        assertEquals(TurnPhase.CHOSE_CARDS, game.getTurnPhase());
        setCardTurn();
        assertEquals(TurnPhase.PLACE_WORKERS, game.getTurnPhase());
        setPlaceWorkerTurn(game);
        assertEquals(TurnPhase.NORMAL, game.getTurnPhase());
    }


    @Test
    void checkIncorrectOperationPlaceWorkerTurn(){
        Game game=setGameWithoutCards();
        Exception exception = assertThrows(IllegalTurnPhaseException.class, () -> {
            game.move(new Position(1,1), new Position(0,0));
        });

        Exception exception2 = assertThrows(IllegalTurnPhaseException.class, () -> {
            game.build(new Position(1,1), new Position(0,0), false);
        });

    }

    @Test
    void checkIncorrectOperationNormalTurn() throws PositionOutOfBoundsException {
        setCardTurn();
        setPlaceWorkerTurn(game);

        Exception exception3 = assertThrows(IllegalTurnPhaseException.class, () -> {
            game.place( new Position(0,0));
        });

    }



    @Test
    void checkCorrectPlayer(){
        setCardTurn();
        assertTrue(game.getCurrentPlayer().getUuid().equals(player0.getUuid()));

    }

    @Test
    void checkIncorrectPlayer(){
        setCardTurn();
        assertFalse(game.getCurrentPlayer().getUuid().equals(player1.getUuid()));

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
        assertEquals(TurnPhase.CHOSE_CARDS, game.getTurnPhase());
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
        game.setPlayerCard("Apollo");
        assertEquals(2, game.getChosenCards().size());
    }

    @Test
    void chooseCorrectlyTwoCard(){
        ArrayList<String> chosenCards = new ArrayList<>();
        chosenCards.add("Apollo");
        chosenCards.add("Artemis");
        chosenCards.add("Athena");
        game.setChosenCards(chosenCards);
        game.setPlayerCard("Apollo");
        assertEquals(2, game.getChosenCards().size());
        game.nextTurn();
        game.setPlayerCard("Artemis");
        assertEquals(1, game.getChosenCards().size());

    }

    @Test
    void setFirstTurnCompletely(){
        ArrayList<String> chosenCards = new ArrayList<>();
        chosenCards.add("Apollo");
        chosenCards.add("Artemis");
        chosenCards.add("Athena");
        game.setChosenCards(chosenCards);
        assertEquals(TurnPhase.CHOSE_CARDS, game.getTurnPhase());
        game.nextTurn();

        game.setPlayerCard("Apollo");
        assertEquals(TurnPhase.CHOSE_CARDS, game.getTurnPhase());
        assertEquals(2, game.getChosenCards().size());
        game.nextTurn();

        game.setPlayerCard("Artemis");
        assertEquals(TurnPhase.CHOSE_CARDS, game.getTurnPhase());
        assertEquals(1, game.getChosenCards().size());
        game.nextTurn();


        assertEquals(TurnPhase.CHOSE_CARDS, game.getTurnPhase());
        assertEquals(0, game.getChosenCards().size());
        assertFalse(game.isSetFirstPlayer());

        game.firstTurn(player0);
        assertTrue(game.isSetFirstPlayer());
        game.nextTurn();

        assertTrue(game.isSetFirstPlayer());
        assertEquals(TurnPhase.PLACE_WORKERS, game.getTurnPhase());

    }

    @Test
    void isAnyWorkerPlaced(){
        Game game = setGameWithoutCards();
        assertTrue(game.isAnyWorkerNotPlaced());
    }


    @Test
    void isAnyWorkerNotPlaced() throws PositionOutOfBoundsException {
        Game game = setGameWithoutCards();
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
        Game game= setGameWithoutCards();
        Position workerPosition = new Position (0,0);
        assertNotEquals(game.place(workerPosition),null);
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
        assertNotEquals(null, game.place(workerPosition));
    }

    @Test
    void isFeasibleDefaultMove() throws PositionOutOfBoundsException {
        Game game= setGameWithoutCards();
        setPlaceWorkerTurn(game);
        Position workerPosition = game.getCurrentPlayer().getWorkerPosition(0);
        Position destPosition = new Position(0,1);
        assertTrue(game.isAllowedToMove(workerPosition));
        assertTrue(game.isRequiredToMove());
        game.isFeasibleMove(workerPosition,destPosition);
        assertTrue(game.checkCurrentWorker(workerPosition));
    }

    @Test
    void defaultMove() throws PositionOutOfBoundsException {
        Game game= setGameWithoutCards();
        setPlaceWorkerTurn(game);
        Position workerPosition = game.getCurrentPlayer().getWorkerPosition(0);
        Position destPosition = new Position(0,1);
        assertTrue(game.isAllowedToMove(workerPosition));
        assertTrue(game.isRequiredToMove());
        game.isFeasibleMove(workerPosition,destPosition);
        assertTrue(game.checkCurrentWorker(workerPosition));
        game.move(workerPosition,destPosition);
        assertFalse(game.isAllowedToMove());
        assertFalse(game.isRequiredToMove());
        Position workerPosition2 = game.getCurrentPlayer().getWorkerPosition(1);
        assertFalse(game.checkCurrentWorker(workerPosition2));
    }

    @Test
    void isNotFeasibleDefaultMove() throws PositionOutOfBoundsException {
        Game game= setGameWithoutCards();
        setPlaceWorkerTurn(game);
        Position workerPosition = game.getCurrentPlayer().getWorkerPosition(0);
        Position workerPosition2 = game.getCurrentPlayer().getWorkerPosition(1);
        Position destPosition = new Position(0,0);
        assertTrue(game.isAllowedToMove(workerPosition));
        assertTrue(game.isAllowedToMove(workerPosition2));
        assertTrue(game.isRequiredToMove());
        assertFalse(game.isFeasibleMove(workerPosition,destPosition));

    }


    @Test
    void isFeasibleDefaultBuild() throws PositionOutOfBoundsException {

        Game game= setGameWithoutCards();
        setPlaceWorkerTurn(game);
        Position workerPosition = game.getCurrentPlayer().getWorkerPosition(0);

        Position destPosition = new Position(0,1);
        assertTrue(game.isAllowedToMove(workerPosition));
        assertTrue(game.isRequiredToMove());
        game.isFeasibleMove(workerPosition,destPosition);
        assertTrue(game.checkCurrentWorker(workerPosition));
        game.move(workerPosition,destPosition);
        assertTrue(game.isRequiredToBuild());
        assertTrue(game.isAllowedToBuild());
        Position destBuildPosition = new Position(0,0);
        Position workerPosition2 = game.getCurrentPlayer().getWorkerPosition(0);
        assertTrue(game.isFeasibleBuild(workerPosition2,destBuildPosition, false));

    }

    @Test
    void DefaultBuild() throws PositionOutOfBoundsException {
        Game game= setGameWithoutCards();
        setPlaceWorkerTurn(game);
        Position workerPosition = game.getCurrentPlayer().getWorkerPosition(0);
        Position destPosition = new Position(0,1);
        assertTrue(game.isAllowedToMove(workerPosition));
        assertTrue(game.isRequiredToMove());
        game.isFeasibleMove(workerPosition,destPosition);
        assertTrue(game.checkCurrentWorker(workerPosition));
        game.move(workerPosition,destPosition);
        assertTrue(game.isRequiredToBuild());
        assertTrue(game.isAllowedToBuild());
        Position destBuildPosition = new Position(0,0);
        Position workerPosition2 = game.getCurrentPlayer().getWorkerPosition(0);
        assertTrue(game.isFeasibleBuild(workerPosition2,destBuildPosition, false));
        game.build(workerPosition2,destBuildPosition, false);
        assertFalse(game.isRequiredToBuild());
        assertFalse(game.isAllowedToBuild());

    }


    @Test
    void isNotFeasibleDefaultBuild() throws PositionOutOfBoundsException {

        Game game= setGameWithoutCards();
        setPlaceWorkerTurn(game);
        Position workerPosition = game.getCurrentPlayer().getWorkerPosition(0);
        Position destPosition = new Position(0,1);
        assertTrue(game.isAllowedToMove(workerPosition));
        assertTrue(game.isRequiredToMove());
        game.isFeasibleMove(workerPosition,destPosition);
        assertTrue(game.checkCurrentWorker(workerPosition));
        game.move(workerPosition,destPosition);
        Position destBuildPosition = new Position(0,0);
        Position workerPosition2 = game.getCurrentPlayer().getWorkerPosition(0);
        assertFalse(game.isFeasibleBuild(workerPosition2,destPosition, false));
        assertFalse(game.isFeasibleBuild(workerPosition2,destBuildPosition,true));
    }

    @Test
    void checkHasLost() throws PositionOutOfBoundsException {
        Game game= setGameWithoutCards();
        setPlaceWorkerTurn(game);
        Position workerPosition = game.getCurrentPlayer().getWorkerPosition(0);
        Position destPosition = new Position(0,1);
        assertTrue(game.isAllowedToMove(workerPosition));
        assertTrue(game.isRequiredToMove());
        game.isFeasibleMove(workerPosition,destPosition);
        assertTrue(game.checkCurrentWorker(workerPosition));
        game.move(workerPosition,destPosition);
        assertFalse(game.checkHasLost());

    }

    @Test
    void checkHasLost2() throws PositionOutOfBoundsException {
        Game game= setGameWithoutCards();
        setPlaceWorkerTurn(game);
        assertTrue(game.isRequiredToMove());
        assertTrue(game.isRequiredToBuild());
        assertFalse(game.checkHasLost());
    }

    @Test
    void undo() throws PositionOutOfBoundsException {
        setCardTurn();
        setPlaceWorkerTurn(game);
        game.nextTurn();
        assertEquals(TurnPhase.NORMAL, game.getTurnPhase());
        Position workerPosition = game.getCurrentPlayer().getWorkerPosition(0);
        Position destPosition = new Position(0,1);
        assertFalse(game.undo());
        assertTrue(game.isAllowedToMove(workerPosition));
        assertTrue(game.isRequiredToMove());
        game.move(workerPosition,destPosition);
        Position workerPosition2 = game.getCurrentPlayer().getWorkerPosition(0);
        assertFalse(game.isRequiredToMove());
        assertTrue(game.checkCurrentWorker(workerPosition2));
        assertTrue(game.undo());
        assertTrue(game.isAllowedToMove(game.getCurrentPlayer().getWorkerPosition(0)));
        assertTrue(game.isRequiredToMove());
        assertTrue(game.checkCurrentWorker(game.getCurrentPlayer().getWorkerPosition(0)));
        assertTrue(game.checkCurrentWorker(game.getCurrentPlayer().getWorkerPosition(1)));
        assertFalse(game.undo());
    }

    @Test
    void completeGame1() throws PositionOutOfBoundsException {
        ArrayList<String> chosenCards = new ArrayList<>();
        chosenCards.add("Apollo");
        chosenCards.add("Artemis");
        chosenCards.add("Athena");
        game.setChosenCards(chosenCards);
        assertEquals(game.getTurnPhase(), TurnPhase.CHOSE_CARDS);
        game.nextTurn();

        game.setPlayerCard("Apollo");
        assertEquals(game.getTurnPhase(), TurnPhase.CHOSE_CARDS);
        game.nextTurn();

        game.setPlayerCard("Artemis");
        assertEquals(game.getTurnPhase(), TurnPhase.CHOSE_CARDS);
        game.nextTurn();

        //game.setPlayerCard("Athena");
        assertEquals(game.getTurnPhase(), TurnPhase.CHOSE_CARDS);
        System.out.println("ciao");
        System.out.println(game.getCurrentPlayer().getCard().getName());

        //game.firstTurn(player0);
        game.firstTurn(player0);
        game.nextTurn();

        assertEquals(game.getTurnPhase(), TurnPhase.PLACE_WORKERS);
        /*Exception exception = assertThrows(IllegalTurnPhaseException.class, () -> {
            game.move(new Position(1,1), new Position(0,0));
        });*/
        game.place(new Position(0,0));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(0,0));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(1,1));
        assertFalse(game.isAnyWorkerNotPlaced());

        game.nextTurn();
        /*Exception exception1 = assertThrows(IllegalTurnPhaseException.class, () -> {
            game.move(new Position(1,1), new Position(0,0));
        });*/
        assertEquals(game.getTurnPhase(), TurnPhase.PLACE_WORKERS);
        game.place(new Position(2,2));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(0,0));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(3,3));
        assertFalse(game.isAnyWorkerNotPlaced());

        game.nextTurn();
        /*
        Exception exception2 = assertThrows(IllegalTurnPhaseException.class, () -> {
            game.move(new Position(1,1), new Position(0,0));
        });*/
        assertEquals(game.getTurnPhase(), TurnPhase.PLACE_WORKERS);
        game.place(new Position(4,4));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(3,3));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(4,3));
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();

        if(game.getCurrentPlayer().getCard().getName().equals("Apollo")) {
            System.out.println("Apollo");
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);
            assertTrue(game.isFeasibleMove(game.getCurrentPlayer().getWorkerPosition(1), new Position(2, 2)));
            game.move(game.getCurrentPlayer().getWorkerPosition(1), new Position(2, 2));
            assertFalse(game.isAllowedToMove());
            assertTrue(game.isAllowedToBuild());
            assertFalse(game.isRequiredToMove());
            assertTrue(game.isRequiredToBuild());
            assertTrue(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(1), new Position(1, 2), false));
            game.build(game.getCurrentPlayer().getWorkerPosition(1), new Position(1, 2), false);
            assertFalse(game.isRequiredToMove());
            assertFalse(game.isRequiredToBuild());
            game.nextTurn();
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);


        } else if(game.getCurrentPlayer().getCard().getName().equals("Artemis")){
            System.out.println("Artemis");
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);
            assertTrue(game.isFeasibleMove(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1)));
            game.move(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1));
            assertTrue(game.isAllowedToMove());
            assertTrue(game.isAllowedToBuild());
            assertTrue(game.isRequiredToBuild());
            assertFalse(game.isRequiredToMove());
            assertTrue(game.isFeasibleMove(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 2)));
            assertFalse(game.isFeasibleMove(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0)));
            game.move(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 2));
            assertFalse(game.isAllowedToMove());
            assertFalse(game.isRequiredToMove());
            assertTrue(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1), false));
            game.build(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1), false);
            assertFalse(game.isRequiredToMove());
            assertFalse(game.isRequiredToBuild());
            game.nextTurn();
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);

        } else{
            System.out.println("Athena");
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);
            assertTrue(game.isFeasibleMove(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1)));
            game.move(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1));
            assertFalse(game.isAllowedToMove());
            assertFalse(game.isRequiredToMove());
            assertTrue(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), false));
            game.build(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), false);
            assertFalse(game.isRequiredToMove());
            assertFalse(game.isRequiredToBuild());
            game.nextTurn();
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);
        }

    }

    @Test
    void completeGame2() throws PositionOutOfBoundsException {
        ArrayList<String> chosenCards = new ArrayList<>();
        chosenCards.add("Atlas");
        chosenCards.add("Demeter");
        chosenCards.add("Hephaestus");
        game.setChosenCards(chosenCards);
        assertEquals(game.getTurnPhase(), TurnPhase.CHOSE_CARDS);
        game.nextTurn();

        game.setPlayerCard("Atlas");
        assertEquals(game.getTurnPhase(), TurnPhase.CHOSE_CARDS);
        game.nextTurn();

        game.setPlayerCard("Demeter");
        assertEquals(game.getTurnPhase(), TurnPhase.CHOSE_CARDS);
        game.nextTurn();

        //game.setPlayerCard("Hephaestus");
        assertEquals(game.getTurnPhase(), TurnPhase.CHOSE_CARDS);
        //game.firstTurn(player0);
        game.firstTurn(player0);
        game.nextTurn();

        assertEquals(game.getTurnPhase(), TurnPhase.PLACE_WORKERS);
        /*
        Exception exception = assertThrows(IllegalTurnPhaseException.class, () -> {
            game.move(new Position(1,1), new Position(0,0));
        });*/
        game.place(new Position(0,0));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(0,0));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(1,1));
        assertFalse(game.isAnyWorkerNotPlaced());

        game.nextTurn();
        /*
        Exception exception1 = assertThrows(IllegalTurnPhaseException.class, () -> {
            game.move(new Position(1,1), new Position(0,0));
        });*/
        assertEquals(game.getTurnPhase(), TurnPhase.PLACE_WORKERS);
        game.place(new Position(2,2));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(0,0));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(3,3));
        assertFalse(game.isAnyWorkerNotPlaced());

        game.nextTurn();
        /*
        Exception exception2 = assertThrows(IllegalTurnPhaseException.class, () -> {
            game.move(new Position(1,1), new Position(0,0));
        });*/
        assertEquals(game.getTurnPhase(), TurnPhase.PLACE_WORKERS);
        game.place(new Position(4,4));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(3,3));
        assertTrue(game.isAnyWorkerNotPlaced());
        game.place(new Position(4,3));
        assertFalse(game.isAnyWorkerNotPlaced());
        game.nextTurn();

        if(game.getCurrentPlayer().getCard().getName().equals("Atlas")) {
            System.out.println("Atlas");
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);
            assertTrue(game.isFeasibleMove(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1)));
            game.move(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1));
            assertFalse(game.isAllowedToMove());
            assertTrue(game.isAllowedToBuild());
            assertFalse(game.isRequiredToMove());
            assertTrue(game.isRequiredToBuild());
            assertTrue(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), true));
            game.build(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), true);
            assertFalse(game.isRequiredToMove());
            assertFalse(game.isRequiredToBuild());
            game.nextTurn();
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);


        } else if(game.getCurrentPlayer().getCard().getName().equals("Demeter")){
            System.out.println("Demeter");
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);
            assertTrue(game.isFeasibleMove(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1)));
            game.move(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1));
            assertFalse(game.isAllowedToMove());
            assertTrue(game.isAllowedToBuild());
            assertTrue(game.isRequiredToBuild());
            assertFalse(game.isRequiredToMove());

            assertTrue(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), false));
            game.build(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), false);
            assertFalse(game.isRequiredToMove());
            assertFalse(game.isRequiredToBuild());
            assertTrue(game.isAllowedToBuild());
            assertFalse(game.isAllowedToMove());


            assertFalse(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), false));
            assertTrue(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 2), false));
            game.build(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 2), false);
            assertFalse(game.isRequiredToMove());
            assertFalse(game.isRequiredToBuild());
            assertFalse(game.isAllowedToBuild());
            assertFalse(game.isAllowedToMove());

            game.nextTurn();
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);

        } else{
            System.out.println("Hephaestus");
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);
            assertTrue(game.isFeasibleMove(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1)));
            game.move(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 1));
            assertFalse(game.isAllowedToMove());
            assertTrue(game.isAllowedToBuild());
            assertTrue(game.isRequiredToBuild());
            assertFalse(game.isRequiredToMove());

            assertTrue(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), false));
            game.build(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), false);
            assertFalse(game.isRequiredToMove());
            assertFalse(game.isRequiredToBuild());
            assertTrue(game.isAllowedToBuild());
            assertFalse(game.isAllowedToMove());

            assertFalse(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 2), false));
            assertFalse(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), true));
            assertTrue(game.isFeasibleBuild(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), false));
            game.build(game.getCurrentPlayer().getWorkerPosition(0), new Position(0, 0), false);
            assertFalse(game.isRequiredToMove());
            assertFalse(game.isRequiredToBuild());
            assertFalse(game.isAllowedToBuild());
            assertFalse(game.isAllowedToMove());

            game.nextTurn();
            assertEquals(game.getTurnPhase(), TurnPhase.NORMAL);
        }

    }

}
