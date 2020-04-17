package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.StrategyNameNotFound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {

    static Player player1 = new Player("player1");


    /**
     * check if the card is set
     * @throws StrategyNameNotFound
     */
    @Test
    void checkIfCardIsSet() throws StrategyNameNotFound {
        Card card = new Card("Default","Default", "Default", "Default", "Default", "Default");
        assertTrue(player1.setCard(card));
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
     * check if workers are created
     */
    @Test
    void checkAddWorker(){
        player1.initWorkers(2);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();
        int numWorker;
        numWorker=player1.addWorker(worker1);
        assertTrue(player1.addWorker(worker2)==1);
    }

    /**
     * 3 workers are initialized but only one is placed
     * check if there are workers non placed yet
     */
    @Test
    void checkWorkersToPlace(){
        player1.initWorkers(3);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();
        Worker worker3 = new Worker();
        int numWorkersToPlace;
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

}