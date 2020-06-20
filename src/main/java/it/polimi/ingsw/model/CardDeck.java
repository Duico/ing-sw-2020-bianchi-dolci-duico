package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.ReadConfigurationXMLException;
import it.polimi.ingsw.model.exception.StrategyNameNotFound;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardDeck implements Serializable {
    private ArrayList<Card> cardDeck;

//    public CardDeck() throws IOException, SAXException, ParserConfigurationException, ReadConfigurationXMLException {
//        this("./card-config.xml");
//    }
    public CardDeck(File file) throws ReadConfigurationXMLException, ParserConfigurationException, SAXException, IOException {
        cardDeck = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        readConfigurationXML(document);
    }
    public CardDeck(String pathname) throws IOException, SAXException, ParserConfigurationException, ReadConfigurationXMLException{
        this(new File(pathname));
    }
//    public CardDeck(URL resource) throws IOException, ReadConfigurationXMLException, ParserConfigurationException, SAXException {
//        this(resource.getFile());
//    }

    public CardDeck(InputStream resourceAsStream) throws ParserConfigurationException, IOException, SAXException, ReadConfigurationXMLException {
        cardDeck = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(resourceAsStream);
        readConfigurationXML(document);
    }

    private void readConfigurationXML(Document document) throws ReadConfigurationXMLException {
        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName("card");

        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                String name = getPropertyName(eElement, "name");
                String moveStrategy = getPropertyName(eElement, "moveStrategy");
                String buildStrategy = getPropertyName(eElement, "buildStrategy");
                String winStrategy = getPropertyName(eElement,"winStrategy");
                String blockStrategy = getPropertyName(eElement, "blockStrategy");
                String opponentStrategy = getPropertyName(eElement,"opponentStrategy");

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

    public boolean existsCardByName(String name){
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
                return cardDeck.get(i);
                //break;
        }
        return null;

    }
    private String getPropertyName(Element node, String tagName){
        Node leaf = node.getElementsByTagName(tagName).item(0);
        return (leaf != null)? leaf.getTextContent():"Default";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardDeck cardDeck1 = (CardDeck) o;
        return Objects.equals(cardDeck, cardDeck1.cardDeck);
    }
    
}

