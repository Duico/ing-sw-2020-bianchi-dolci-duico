package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.ReadConfigurationXMLException;
import it.polimi.ingsw.model.exception.StrategyNameNotFound;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardDeck implements Serializable {
    private ArrayList<Card> cardDeck;

    public CardDeck() throws IOException, SAXException, ParserConfigurationException, ReadConfigurationXMLException {
        this("./card-config.xml");
    }

    public CardDeck(String pathname) throws IOException, SAXException, ParserConfigurationException, ReadConfigurationXMLException{
        cardDeck = new ArrayList<>();
        readConfigurationXML(pathname);
    }



    private void readConfigurationXML(String pathname) throws ReadConfigurationXMLException, ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(new File(pathname));
        document.getDocumentElement().normalize();
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

                try {
                    cardDeck.add(new Card(name, moveStrategy, buildStrategy, winStrategy, blockStrategy, opponentStrategy));
                }catch (StrategyNameNotFound e){
                    System.err.println("Strategy name not found. Check your XML file.");
                    e.printStackTrace();
                }
            }
        }
        if(cardDeck.size() < 3){ //FIX too vague
            throw new ReadConfigurationXMLException();
        }
    }

    public Card getCard(int i) {
        return cardDeck.get(i);
    }
    public List<String> getCardNames(){
        ArrayList<String> cardNames  = new ArrayList<String>();
        for(Card card: cardDeck){
            cardNames.add(card.getName());
        }
        return cardNames;
    }

//    /**
//     * Picks [numPlayers] random cards to be assigned to all players in the game
//     * @param numPlayers Number of cards to extract,should be equal to the number of players
//     * @return Deck of [numPlayers] cards
//     */
//    public ArrayList<Card> pickRandom(int numPlayers) {
//        int rand;
//        ArrayList<Card> randomDeck = new ArrayList<>();
//        int i = 0;
//        while(i < numPlayers)
//        {
//            Card randCard ;
//            do {
//                rand = (int) Math.floor( (Math.random() * (double) cardDeck.size()) );
//                randCard=cardDeck.get(rand);
//            }
//            while(randomDeck.contains(randCard));
//            randomDeck.add(randCard);
//            i++;
//        }
//        return randomDeck;
//    }

    public boolean existCard(String name){
        for(int i=0; i<cardDeck.size();i++){
            if(name.equals(cardDeck.get(i).getName()))
                return true;
        }
        return false;
    }

    public Card getCardByName(String name){
        int i;
        for(i=0; i<cardDeck.size();i++){
            if(name.equals(cardDeck.get(i).getName()))
                break;
        }
        return cardDeck.get(i);

    }
}

