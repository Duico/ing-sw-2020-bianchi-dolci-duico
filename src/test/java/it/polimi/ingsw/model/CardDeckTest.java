/*
package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CardDeckTest {

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
        Exception exceptionCardDeck =
        assertThrows(ReadConfigurationXMLException.class, () ->
            {
                new CardDeck("./card-config_test1.xml");
            }
        );

    }
    @Test
    void getCard() {

    }

    @Test
    void pickRandom() {
    }
}*/
