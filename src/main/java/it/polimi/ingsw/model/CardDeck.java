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

//Build Document
        Document document = builder.parse(new File("CardDeck.xml"));    //toDo

//Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

//Here comes the root node
        Element root = document.getDocumentElement();
        System.out.println(root.getNodeName());

//Get all employees
        NodeList nList = document.getElementsByTagName("card");


        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                //Print each employee's detail
                Element eElement = (Element) node;
                cardDeck[temp]= new Card();
                cardDeck[temp].setName(eElement.getElementsByTagName("id").item(0).getTextContent());
                cardDeck[temp].setMoveStrategy(eElement.getElementsByTagName("moveStrategy").item(0).getTextContent());
                cardDeck[temp].setBuildStrategy(eElement.getElementsByTagName("buildStrategy").item(0).getTextContent());
                cardDeck[temp].setWinStrategy(eElement.getElementsByTagName("winStrategy").item(0).getTextContent());
                cardDeck[temp].setBlockStrategy(eElement.getElementsByTagName("blockStrategy").item(0).getTextContent());
                cardDeck[temp].setOpponentStrategy(eElement.getElementsByTagName("opponentStrategy").item(0).getTextContent());
            }
        }
    }

    public Card getCardDeck(int i) {
        return cardDeck[i];
    }

    public void setCardDeck(Card[] cardDeck) {
        this.cardDeck = cardDeck;
    }
}

