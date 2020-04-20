package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.ReadConfigurationXMLException;
import it.polimi.ingsw.model.strategy.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {

    @Test
    void standardDeckXMLTest(){
        try {
            CardDeck cd = new CardDeck("./card-config.xml");
            Card card0 = cd.getCard(0);
            Card card1 = cd.getCard(1);
            Card card2 = cd.getCard(2);
            Card card3 = cd.getCard(3);
            Card card4 = cd.getCard(4);
            Card card5 = cd.getCard(5);
            Card card6 = cd.getCard(6);
            Card card7 = cd.getCard(7);
            Card card8 = cd.getCard(8);

            assertEquals("Apollo", card0.getName());
            assertEquals("Artemis", card1.getName());
            assertEquals("Athena", card2.getName());
            assertEquals("Atlas", card3.getName());
            assertEquals("Demeter", card4.getName());
            assertEquals("Hephaestus",card5.getName());
            assertEquals("Minotaur", card6.getName());
            assertEquals("Pan",     card7.getName());
            assertEquals("Prometheus", card8.getName());
            assertEquals(SwapOpponent.class, card0.getOpponentStrategy().getClass());
            assertEquals(MultipleMove.class, card1.getMoveStrategy().getClass());
            //assertEquals(); //TODO check numMoves == 2
            assertEquals(UpMoveBlock.class, card2.getBlockStrategy().getClass());
            assertEquals(DomeBuild.class, card3.getBuildStrategy().getClass());
            assertEquals(DownMoveWin.class, card7.getWinStrategy().getClass());

            //check that exactly 9 cards have been added
            assertThrows(IndexOutOfBoundsException.class, () -> {
                cd.getCard(9);
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    void readXMLTest0() {
        try {
            CardDeck cd = new CardDeck("./card-config_test0.xml");
            Card card0 = cd.getCard(0);
            Card card1 = cd.getCard(1);
            Card card2 = cd.getCard(2);
            assertEquals("DefaultONE", card0.getName());
            assertEquals("DefaultTWO", card1.getName());
            assertEquals("DefaultTHREE", card2.getName());
            //check that exactly 3 cards have been added
            assertThrows(IndexOutOfBoundsException.class, () -> {
                cd.getCard(3);
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    void failXMLTest1() throws Exception{
        assertThrows(ReadConfigurationXMLException.class, () ->
            {
                new CardDeck("./card-config_test1.xml");
            }
        );

    }
    @Test
    void getCard() throws Exception{
        CardDeck cd = new CardDeck("./card-config_test0.xml");
        assertEquals(
                new Card("DefaultONE", "Default","Default","Default","Default","Default"),
                cd.getCard(0)
        );
    }

    @Test
    void existsCardTest() throws Exception {
        CardDeck cd = new CardDeck("./card-config.xml");
        assertTrue(cd.existsCardByName("Apollo"));
        assertTrue(cd.existsCardByName("Artemis"));
        assertTrue(cd.existsCardByName("Athena"));
        assertTrue(cd.existsCardByName("Atlas"));
        assertTrue(cd.existsCardByName("Demeter"));
        assertTrue(cd.existsCardByName("Hephaestus"));
        assertTrue(cd.existsCardByName("Minotaur"));
        assertTrue(cd.existsCardByName("Pan"));
        assertTrue(cd.existsCardByName("Prometheus"));

        assertFalse(cd.existsCardByName(""));
        assertFalse(cd.existsCardByName("Pippo"));
        assertFalse(cd.existsCardByName("Pluto"));
        assertFalse(cd.existsCardByName(" "));
    }
}
