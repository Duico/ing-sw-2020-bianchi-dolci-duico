package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.view.event.*;
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

        private void setCardTurn(){
            ArrayList<String> chosenCards = new ArrayList<>();
            chosenCards.add("Atlas");
            chosenCards.add("Artemis");
            chosenCards.add("Pan");
            game.setChosenCards(chosenCards);
            game.nextTurn();

            game.setPlayerCard("Atlas");
            game.nextTurn();

            game.setPlayerCard("Artemis");
            game.nextTurn();

            game.setPlayerCard("Pan");

        }

        private void setFirstPlayer(){
            setCardTurn();
            game.firstTurn(player0);
        }

        private void placeAllWorkers() throws PositionOutOfBoundsException {
            setFirstPlayer();
            Position workerPosition1Player1 = new Position (0,0);
            Position workerPosition2Player1 = new Position (1,1);
            game.place(workerPosition1Player1);
            game.place(workerPosition2Player1);
            game.nextTurn();
            Position workerPosition1Player2 = new Position (2,0);
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
        void checkChallengerCards(){
            ArrayList<String> chosenCards = new ArrayList<>();
            chosenCards.add("Apollo");
            chosenCards.add("Artemis");
            chosenCards.add("Athena");
            Player currentPlayer = game.getCurrentPlayer();
            controller.challengerCards(new ChallengerCardViewEvent(currentPlayer, chosenCards));
            assertNotEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkIllegalCardNamesList(){
            ArrayList<String> chosenCards = new ArrayList<>();
            chosenCards.add("Apollo");
            chosenCards.add("Artemis");
            chosenCards.add("");
            Player currentPlayer = game.getCurrentPlayer();
            controller.challengerCards(new ChallengerCardViewEvent(currentPlayer, chosenCards));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkWrongPlayer(){
            ArrayList<String> chosenCards = new ArrayList<>();
            chosenCards.add("Apollo");
            chosenCards.add("Artemis");
            chosenCards.add("Athena");
            Player currentPlayer = game.getCurrentPlayer();
            Player notCurrentPlayer = null;
            for(Player player:players){
                if(!player.getUuid().equals(currentPlayer.getUuid()))
                    notCurrentPlayer=player;
            }
            controller.challengerCards(new ChallengerCardViewEvent(notCurrentPlayer, chosenCards));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkSetPlayerCard(){
            ArrayList<String> chosenCards = new ArrayList<>();
            chosenCards.add("Apollo");
            chosenCards.add("Artemis");
            chosenCards.add("Athena");
            Player currentPlayer = game.getCurrentPlayer();
            controller.challengerCards(new ChallengerCardViewEvent(currentPlayer, chosenCards));
            currentPlayer=game.getCurrentPlayer();
            controller.setPlayerCard(new CardViewEvent(currentPlayer,"Apollo"));
            assertNotEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkSetWrongCard(){
            ArrayList<String> chosenCards = new ArrayList<>();
            chosenCards.add("Apollo");
            chosenCards.add("Artemis");
            chosenCards.add("Athena");
            Player currentPlayer = game.getCurrentPlayer();
            controller.challengerCards(new ChallengerCardViewEvent(currentPlayer, chosenCards));
            currentPlayer=game.getCurrentPlayer();
            controller.setPlayerCard(new CardViewEvent(currentPlayer,"Atlas"));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkSetCardWrongPlayer(){
            ArrayList<String> chosenCards = new ArrayList<>();
            chosenCards.add("Apollo");
            chosenCards.add("Artemis");
            chosenCards.add("Athena");
            Player currentPlayer = game.getCurrentPlayer();
            controller.challengerCards(new ChallengerCardViewEvent(currentPlayer, chosenCards));
            currentPlayer=game.getCurrentPlayer();
            Player notCurrentPlayer = null;
            for(Player player:players){
                if(!player.getUuid().equals(currentPlayer.getUuid()))
                    notCurrentPlayer=player;
            }
            controller.setPlayerCard(new CardViewEvent(notCurrentPlayer, "Apollo"));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkCorrectChooseCardTurn(){
            ArrayList<String> chosenCards = new ArrayList<>();
            chosenCards.add("Apollo");
            chosenCards.add("Artemis");
            chosenCards.add("Athena");
            Player currentPlayer = game.getCurrentPlayer();
            controller.challengerCards(new ChallengerCardViewEvent(currentPlayer, chosenCards));
            currentPlayer=game.getCurrentPlayer();
            controller.setPlayerCard(new CardViewEvent(currentPlayer,"Artemis"));
            assertNotEquals(currentPlayer, game.getCurrentPlayer());
            currentPlayer=game.getCurrentPlayer();
            controller.setPlayerCard(new CardViewEvent(currentPlayer,"Athena"));
            assertNotEquals(currentPlayer, game.getCurrentPlayer());
            currentPlayer=game.getCurrentPlayer();
            assertEquals(currentPlayer.isChallenger(), true);
        }


        @Test
        void checkIsChallenger(){
            setCardTurn();
            assertEquals(game.getCurrentPlayer().isChallenger(), true);
        }

        @Test
        void checkCorrectFirstPlayer(){
            setCardTurn();
            Player currentPlayer = game.getCurrentPlayer();
            Player notCurrentPlayer = null;
            for(Player player:players){
                if(!player.getUuid().equals(currentPlayer.getUuid()))
                    notCurrentPlayer=player;
            }
            controller.setFirstPlayer(new FirstPlayerViewEvent(currentPlayer, notCurrentPlayer ));
            assertNotEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkWrongFirstPlayer(){
            setCardTurn();
            Player currentPlayer = game.getCurrentPlayer();
            controller.setFirstPlayer(new FirstPlayerViewEvent(currentPlayer, currentPlayer));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkIsNotCurrentPlayerChallenger(){
            setCardTurn();
            Player currentPlayer = game.getCurrentPlayer();
            Player notCurrentPlayer = null;
            for(Player player:players){
                if(!player.getUuid().equals(currentPlayer.getUuid()))
                    notCurrentPlayer=player;
            }
            assertEquals(notCurrentPlayer.isChallenger(), false);
            controller.setFirstPlayer(new FirstPlayerViewEvent(notCurrentPlayer, notCurrentPlayer));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkFirstPlayerAlreadySet(){
            setCardTurn();
            Player challenger = game.getCurrentPlayer();
            assertTrue(challenger.isChallenger());
            Player notCurrentPlayer = null;
            for(Player player:players){
                if(!player.getUuid().equals(challenger.getUuid()))
                    notCurrentPlayer=player;
            }
            controller.setFirstPlayer(new FirstPlayerViewEvent(challenger, notCurrentPlayer ));
            assertNotEquals(challenger, game.getCurrentPlayer());
            Player currentPlayer = game.getCurrentPlayer();
            controller.setFirstPlayer(new FirstPlayerViewEvent(challenger, notCurrentPlayer ));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkIllegalFirstPlayer(){
            setCardTurn();
            Player currentPlayer= game.getCurrentPlayer();
            Player illegalPlayer = new Player("wrong");
            controller.setFirstPlayer(new FirstPlayerViewEvent(currentPlayer, illegalPlayer ));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkCorrectFirstPlayerPlaceWorker() throws PositionOutOfBoundsException {
            setFirstPlayer();
            Player currentPlayer = game.getCurrentPlayer();
            controller.place(new PlaceViewEvent(currentPlayer, new Position(0,0)));
            controller.place(new PlaceViewEvent(currentPlayer, new Position(0,1)));
            controller.endTurn(new EndTurnViewEvent(currentPlayer));
            assertNotEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkFirstPlayerPlaceWorkerWrongPosition() throws PositionOutOfBoundsException {
            setFirstPlayer();
            Player currentPlayer = game.getCurrentPlayer();
            controller.place(new PlaceViewEvent(currentPlayer, new Position(0,0)));
            controller.place(new PlaceViewEvent(currentPlayer, new Position(0,0)));
            controller.endTurn(new EndTurnViewEvent(currentPlayer));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkWrongPlayerPlaceWorker() throws PositionOutOfBoundsException {
            setFirstPlayer();
            Player currentPlayer = game.getCurrentPlayer();
            assertTrue(!player1.getUuid().equals(currentPlayer.getUuid()));
            controller.place(new PlaceViewEvent(player1, new Position(0,0)));
            controller.place(new PlaceViewEvent(player1, new Position(0,1)));
            controller.endTurn(new EndTurnViewEvent(player1));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkIllegalNumberOfWorkersPlaced() throws PositionOutOfBoundsException {
            setFirstPlayer();
            Player currentPlayer = game.getCurrentPlayer();
            controller.place(new PlaceViewEvent(currentPlayer, new Position(0,0)));
            controller.place(new PlaceViewEvent(currentPlayer, new Position(0,1)));
            controller.place(new PlaceViewEvent(currentPlayer, new Position(0,2)));
            controller.endTurn(new EndTurnViewEvent(currentPlayer));
            assertNotEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkIsRequiredToMove() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
            controller.endTurn(new EndTurnViewEvent(currentPlayer));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkIsRequiredToBuild() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
            game.move(new Position(0,0), new Position(0,1));
            controller.endTurn(new EndTurnViewEvent(currentPlayer));
            assertEquals(currentPlayer, game.getCurrentPlayer());
        }

        @Test
        void checkCorrectMove() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
            Position startPosition = currentPlayer.getWorkerPosition(0);
            Position destinationPosition =  new Position(0,1);
            game.move(startPosition,destinationPosition);
            assertNotEquals(startPosition, game.getCurrentPlayer().getWorkerPosition(0));
            assertEquals(game.getCurrentPlayer().getWorkerPosition(0), destinationPosition);
        }

        //check correct move and wrong move
        @Test
        void checkWrongMove() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
            Position startPosition = currentPlayer.getWorkerPosition(0);
            Position destinationPosition =  new Position(0,1);
            game.move(startPosition,destinationPosition);
            assertNotEquals(startPosition, game.getCurrentPlayer().getWorkerPosition(0));
            assertEquals(game.getCurrentPlayer().getWorkerPosition(0), destinationPosition);
            destinationPosition=new Position(1,0);
            assertNotEquals(destinationPosition, game.getCurrentPlayer().getWorkerPosition(0));
        }

        @Test
        void checkIsFeasibleMove() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
//            System.out.println(currentPlayer.getCard().getName());
            Position startPosition = currentPlayer.getWorkerPosition(0);
            //position(1,1) already occupied
            Position destinationPosition =  new Position(1,1);
            controller.move(new MoveViewEvent(currentPlayer, startPosition, destinationPosition));
            assertNotEquals(destinationPosition, game.getCurrentPlayer().getWorkerPosition(0));
            assertEquals(startPosition, game.getCurrentPlayer().getWorkerPosition(0));
        }



       @Test
        void checkCorrectOperations() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
            Position start = currentPlayer.getWorkerPosition(0);
            Position destination = new Position(0,1);
            game.move(start, destination);
            game.build(destination, start, false);
            controller.endTurn(new EndTurnViewEvent(currentPlayer));
            assertNotEquals(currentPlayer, game.getCurrentPlayer());
       }

       @Test
        void checkIsAllowedToBuild() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
            Position start = currentPlayer.getWorkerPosition(0);
            Position destination = new Position(0,1);
            controller.build(new BuildViewEvent(currentPlayer,start, destination,false));
            assertTrue(currentPlayer.getNumBuildsWorker(0)==0);
       }

       @Test
        void checkCorrectBuild() throws PositionOutOfBoundsException {
           placeAllWorkers();
           Player currentPlayer = game.getCurrentPlayer();
           Position start = currentPlayer.getWorkerPosition(0);
           Position destination = new Position(0,1);
           game.move(start, destination);
           controller.build(new BuildViewEvent(currentPlayer,destination,start, false));
           assertTrue(currentPlayer.getNumBuildsWorker(0)==1);
       }

       @Test
        void checkNotFeasibleBuild() throws PositionOutOfBoundsException {
           placeAllWorkers();
           Player currentPlayer = game.getCurrentPlayer();
           Position start = currentPlayer.getWorkerPosition(0);
           Position destination = new Position(0,1);
           Position buildPosition = new Position(1,1);
           game.move(start, destination);
           controller.build(new BuildViewEvent(currentPlayer,destination,buildPosition, false));
           assertTrue(currentPlayer.getNumBuildsWorker(0)==0);
       }

       @Test
        void checkNotCurrentWorkerIdBuild() throws PositionOutOfBoundsException {
           placeAllWorkers();
           Player currentPlayer = game.getCurrentPlayer();
           Position start = currentPlayer.getWorkerPosition(0);
           Position destination = new Position(0,1);
           Position notCurrentWorkerPosition = currentPlayer.getWorkerPosition(1);
           game.move(start, destination);
           controller.build(new BuildViewEvent(currentPlayer,notCurrentWorkerPosition,start, false));
           assertTrue(currentPlayer.getNumBuildsWorker(0)==0);
       }

        @Test
        void checkUndo() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
            Position start = currentPlayer.getWorkerPosition(0);
            Position destination = new Position(0,1);
            controller.move(new MoveViewEvent(currentPlayer, start, destination));
            assertTrue(currentPlayer.getNumMovesWorker(0)==1);
            controller.undo(new UndoViewEvent(currentPlayer));
            assertTrue(currentPlayer.getWorkerPosition(0).equals(start));
        }

        @Test
        void checkTurnInfo() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
            controller.requiredTurnInfo(new InfoViewEvent(currentPlayer));
            assertTrue(game.getCurrentPlayer().getUuid().equals(currentPlayer.getUuid()));
        }

    @Test
    void checkWrongTurnInfo() throws PositionOutOfBoundsException {
        placeAllWorkers();
        Player firstPlayer = game.getCurrentPlayer();
        controller.requiredTurnInfo(new InfoViewEvent(firstPlayer));
        assertTrue(game.getCurrentPlayer().getUuid().equals(firstPlayer.getUuid()));
        game.nextTurn();
        Player currentPlayer = game.getCurrentPlayer();
        controller.requiredTurnInfo(new InfoViewEvent(firstPlayer));
        assertTrue(game.getCurrentPlayer().getUuid().equals(currentPlayer.getUuid()));
    }

        @Test
        void checkWrongTurnPhase() throws PositionOutOfBoundsException {
            placeAllWorkers();
            Player currentPlayer = game.getCurrentPlayer();
            String currentCardName = currentPlayer.getCard().getName();
            controller.setPlayerCard(new CardViewEvent(currentPlayer, "Prometheus"));
            assertTrue(currentPlayer.getCard().getName().equals(currentCardName));
        }

        @Test
        void checkWrongTurnPhaseMove() throws PositionOutOfBoundsException {
            setCardTurn();
            Player currentPlayer = game.getCurrentPlayer();
            Position startPosition= new Position(0,0);
            Position destinationPosition= new Position(1,0);
            controller.move(new MoveViewEvent(currentPlayer, startPosition, destinationPosition));
            assertTrue(game.getCurrentPlayer().getWorkerPosition(0)==null);
        }


}