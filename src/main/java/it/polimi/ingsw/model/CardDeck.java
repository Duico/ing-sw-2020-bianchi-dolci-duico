package it.polimi.ingsw.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CardDeck {
    private ArrayList<Card> cardDeck;

    public CardDeck() throws IOException, SAXException, ParserConfigurationException {
        cardDeck = new ArrayList<>();
        readConfigurationXML();
    }

    private void readConfigurationXML() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new File("CardDeck.xml"));    //toDo

        document.getDocumentElement().normalize();

        Element root = document.getDocumentElement();

        NodeList nList = document.getElementsByTagName("card");


        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                String moveStrategy = eElement.getElementsByTagName("moveStrategy").item(0).getTextContent();
                String buildStrategy = eElement.getElementsByTagName("buildStrategy").item(0).getTextContent();
                String winStrategy = eElement.getElementsByTagName("winStrategy").item(0).getTextContent();
                String blockStrategy = eElement.getElementsByTagName("blockStrategy").item(0).getTextContent();
                String opponentStrategy = eElement.getElementsByTagName("opponentStrategy").item(0).getTextContent();

                cardDeck.add(new Card(name, moveStrategy, buildStrategy, winStrategy, blockStrategy, opponentStrategy));

            }
        }
    }

    public Card getCard(int i) {
        return cardDeck.get(i);
    }

    public ArrayList<Card> pickRandom(int numPlayers)
    {
        int rand;
        ArrayList<Card> deck = new ArrayList<>();
        int i = 0;
        while(i < numPlayers)
        {
            Card randCard ;
            do {
                rand = (int) (Math.random() * (cardDeck.size() + 1));
                randCard=cardDeck.get(rand);
            }
            while(deck.contains(randCard));
            deck.add(randCard);
            i++;
        }
        return deck;
    }
}

