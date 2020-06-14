package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.exception.StrategyNameNotFound;
import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PlayerTest {

    static Player player1 = new Player("player1");
    static Board board;

    @BeforeEach
    void before(){
        board = new Board();
    }


    /**
     * check if the card is set
     * @throws StrategyNameNotFound
     */
    @Test
    void checkIfCardIsSet() throws StrategyNameNotFound {
        Card card = new Card("Default","Default", "Default", "Default", "Default", "Default");
        assertNull(player1.getCard());
        assertTrue(player1.setCard(card));
        assertEquals("Default", player1.getCard().getName());

    }

    /**
     * check if workers are initialized
     */
    @Test
    void checkInitWorkers(){
        int numWorkers=2;
        player1.initWorkers(numWorkers);
        assertTrue(player1.getNumWorkers()==2);
    }

    /**
     * check if worker is correctly set
     */
    @Test
    void checkIsWorkerSet(){
        Worker worker1 = new Worker();
        player1.initWorkers(1);
        player1.addWorker(worker1);
        assertTrue(player1.isWorkerSet(0));
    }
    /**
     * check if workers are created
     */
    @Test
    void checkAddWorker(){
        player1.initWorkers(2);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();
        player1.addWorker(worker1);
        assertTrue(player1.addWorker(worker2).get() == 1);
    }

    /**
     * 3 workers are initialized but only one is placed
     * check if there are workers non placed yet
     */
    @Test
    void checkWorkersToPlace(){
        player1.initWorkers(3);
        Worker worker1 = new Worker();
        player1.addWorker(worker1);
        assertTrue(player1.isAnyWorkerNotPlaced());

    }

    /**
     * 3 workers are initialized and all of them are placed
     * check if there are workers non placed yet
     */
    @Test
    void checkWorkersToPlace2(){
        player1.initWorkers(3);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();
        Worker worker3 = new Worker();
        player1.addWorker(worker1);
        player1.addWorker(worker2);
        player1.addWorker(worker3);
        assertFalse(player1.isAnyWorkerNotPlaced());

    }

    /**
     * check worker current position is set
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkGetWorkerCurrentPosition() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        player1.initWorkers(1);
        Worker worker1 = new Worker();
        Position position = new Position(0,0);
        player1.addWorker(worker1);
        board.setWorker(worker1,position);
        assertTrue(player1.getWorkerPosition(0)!=null);
    }


    /**
     * check if worker has been placed
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsOwnWorkerInPosition() throws PositionOutOfBoundsException {
        player1.initWorkers(1);
        Worker worker1 = new Worker();
        Position position = new Position(0,0);
        player1.addWorker(worker1);
        board.setWorker(worker1,position);
        assertTrue(player1.isOwnWorkerInPosition(position));
    }

    /**
     * check if all worker of the player are reset
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkResetAllWorkers() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        player1.initWorkers(2);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();
        Position position1 = new Position(0,0);
        Position position2 = new Position(0,1);
        Position position3 = new Position(1,1);
        board.setWorker(worker1,position1);
        board.setWorker(worker2,position2);
        board.build(position1,position3,false);
        board.build(position2,position3,false);
        player1.addWorker(worker1);
        player1.addWorker(worker2);
        assertEquals(1,player1.getNumBuildsWorker(0));
        assertEquals(1,player1.getNumBuildsWorker(1));
        player1.resetAllWorkers();
        assertEquals(0,player1.getNumBuildsWorker(0));
        assertEquals(0,player1.getNumBuildsWorker(1));
    }

    /**
     * check if all workers are added to ArrayList workers
     */
    @Test
    void checkWorkersSize(){
        player1.initWorkers(2);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();
        player1.addWorker(worker1);
        player1.addWorker(worker1);
        assertTrue(player1.getNumWorkers()==2);
    }

}