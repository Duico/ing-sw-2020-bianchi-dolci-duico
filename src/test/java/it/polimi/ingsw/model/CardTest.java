package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.StrategyNameNotFound;
import it.polimi.ingsw.model.strategy.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void getDefaultCard() throws Exception{
        Card defaultCard = new Card("Default", "Default", "Default", "Default", "Default", "Default");
        Card constructorDefaultCard = Card.getDefaultCard();
        assertEquals(defaultCard, constructorDefaultCard);
    }

    @Test
    void constructorAthena() throws Exception{
        Card c = new Card("Athena", "Default", "Default", "Default", "UpMove", "Default");
        assertEquals("Athena", c.getName());
        assertEquals(DefaultMove.class, c.getMoveStrategy().getClass());
        assertEquals(DefaultBuild.class, c.getBuildStrategy().getClass());
        assertEquals(UpMoveBlock.class, c.getBlockStrategy().getClass() );
        assertEquals(DefaultOpponent.class, c.getOpponentStrategy().getClass());
        assertEquals(DefaultWin.class, c.getWinStrategy().getClass());
    }
    @Test
    void constructorArtemis() throws Exception{
        Card c = new Card("Artemis", "Multiple(2)", "Default", "Default", "Default", "Default");
        assertEquals("Artemis", c.getName());
        assertEquals(MultipleMove.class, c.getMoveStrategy().getClass());
        //todo check numMoves is 2
        assertEquals(DefaultBuild.class, c.getBuildStrategy().getClass());
        assertEquals(DefaultBlock.class, c.getBlockStrategy().getClass() );
        assertEquals(DefaultOpponent.class, c.getOpponentStrategy().getClass());
        assertEquals(DefaultWin.class, c.getWinStrategy().getClass());
    }

    @Test
    void constructorArtemisFail(){
        assertThrows(StrategyNameNotFound.class, () -> {
            Card c = new Card("Artemis", "Multiple(12)", "Default", "Default", "Default", "Default");
        });
    }

    @Test
    void strategyNameNotFound(){
        assertThrows(StrategyNameNotFound.class, () ->
        {
            Card c = new Card("NotExistent", "Pippo","Pippo","Pippo","Pippo","Pippo");
        }
        );
    }

}