package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * main controller class of choose card scene
 */
public class ChooseCardController implements GuiEventEmitter {

    private GuiEventListener listener;

    @FXML
    public Label header;



    public HBox cards;


    public ChoiceBox firstPlayerChoiceBox;

    public VBox playerBox;

    public VBox waitBox;

    public Label waitLabel;
    @FXML
    private VBox cardsBox;

    private List<String> opponents= new ArrayList<>();

    private Integer requiredNumCards;
    private boolean isChallenger;

    private List<String> cardDeck;
    private final List<String> chosenCards = new ArrayList<>();

    private int numPlayers;

    public void setNumPlayers(int numPlayers){
        this.numPlayers=numPlayers;
    }

    public ChooseCardController(){}

    public void setIsChallenger(boolean isChallenger){
        this.isChallenger=isChallenger;
    }

    public void setCardDeck(List<String> cardDeck){
        this.cardDeck=cardDeck;
    }

    public List<String> getCardDeck(){
        return this.cardDeck;
    }

    /**
     * fills first player choice box with player names
     */
    public void initChoiceBox(){
            firstPlayerChoiceBox.setItems(FXCollections.observableArrayList(opponents));
    }

    public void setOpponents(List<String> opponents){
        this.opponents=opponents;
    }

    /**
     * initialize Hbox containing card images which has to be chosen by players
     * @param requiredNumCards number of cards that challenger player has to choose
     */
    public void askNumCards(Integer requiredNumCards){
        if(requiredNumCards>1){
            initGrid(cardDeck);
        }else{
            initGrid(chosenCards);
        }
    }

    /**
     * fills Hbox with card images
     * @param cards contatins all cards in deck if is challenger player
     *              contains challenger's chosen cards if is not challenger player
     */
    public void initGrid(List<String> cards){
        for(int i=0;i<cards.size();i++){
            CardImageView image = new CardImageView("textures/"+cards.get(i)+".png");
            addEventCard(image, cards.get(i));
            HBox.setMargin(image, new Insets(0, (i==cardDeck.size()-1)?0:10, 0, i==0?0:10));
            this.cards.getChildren().add(image);
        }
    }

    /**
     * add event on card images which can be selected or de-selected by clicking on them
     * @param node card image view
     * @param name name of the card
     */
    private void addEventCard(CardImageView node, String name){
        node.setOnMousePressed(event->{

            if(chosenCards.contains(name))
            {
                chosenCards.remove(name);
                    node.setIsSelected(false);
            }else{
                if(chosenCards.size()<requiredNumCards)
                {
                    System.out.println("add"+name);
                    chosenCards.add(name);
                    node.setIsSelected(true);
                }
            }

        });
    }

    /**
     * loads choose card scene
     * @return
     * @throws IOException
     */
    public Scene chooseCardScene() throws IOException {
        Parent page = FXMLLoader.load(getClass().getResource("/fxml/chooseCard.fxml"));
        Scene scene = new Scene(page);
        return scene;
    }

    /**
     * updates scene setting to invisible cards box and showing wain message
     */
    public void waitChooseCards(){
        Platform.runLater(()->{
            cardsBox.setVisible(false);
            waitLabel.setText("Wait for the other players");
            waitBox.setVisible(true);
        });
    }

    /**
     * updates scene setting to visible first player choice box only to challenger player
     * @param usernames
     */
    public void showChooseFirstPlayer(List<String> usernames){
        setOpponents(usernames);
        Platform.runLater(()->{
            waitBox.setVisible(false);
            playerBox.setVisible(true);
            initChoiceBox();
        });
    }

    /**
     * upadtes scene setting to visible cards box
     * @param cardDeck
     */
    public void loadCards(List<String> cardDeck) {
        Platform.runLater(()->{
            waitBox.setVisible(false);
            cardsBox.setVisible(true);
            if(isChallenger){
                this.requiredNumCards=numPlayers;
                header.setText("Chose " + numPlayers + " cards");
            }else{
                this.requiredNumCards=1;
                header.setText("Chose one card");
            }
            initGrid(cardDeck);
        });
    }

    /**
     * send cards button event
     * @param actionEvent
     */
    public void sendCards(ActionEvent actionEvent){
        checkValidStart();
    }

    /**
     * checks player has chosen cards/card correctly and then emits event
     */
    private void checkValidStart(){
        if(isChallenger){
            if(chosenCards.size()==requiredNumCards)
                emitChallengerCards(chosenCards);
            else
                alert("Check if you have selected "+requiredNumCards+" cards!");
        }else{
            if(chosenCards.size()==1)
                emitChosenCard(chosenCards.get(0));
            else
                alert("Choose a Card!");
        }
    }


    /**
     * shows alert message box
     * @param message message in alert box
     */
    public void alert(String message){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }

    /**
     * @return player's name chosen as first player
     */
    private String getFirstPlayer(){
        Object choiceBoxValue =  firstPlayerChoiceBox.getValue();
        if(choiceBoxValue != null){
            return choiceBoxValue.toString();
        }else{
            return null;
        }
    }

    /**
     * emits first player view event
     */
    public void sendFirstPlayer(ActionEvent actionEvent) {
        emitFirstPlayer(getFirstPlayer());
    }

    /**
     * emits view event containing card chosen by player
     */
    private void emitChosenCard(String chosenCard){
        listener.onChooseCard(chosenCard);
    }

    /**
     * emits view event containing list of cards chosen by challenger player
     */
    private void emitChallengerCards(List<String> challengerCards){
        listener.onChallengeCards(challengerCards);
    }

    /**
     * emits first player view event
     */
    private void emitFirstPlayer(String firstPlayer){
        listener.onFirstPlayer(firstPlayer);
    }

    @Override
    public void setEventListener(GuiEventListener listener) {
        this.listener = listener;
    }
}
