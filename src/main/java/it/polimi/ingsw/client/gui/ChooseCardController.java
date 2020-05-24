package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientEventEmitter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ChooseCardController extends ClientEventEmitter {

    @FXML
    public Label title;

    public ImageView buttonImage;

    public Button startButton;

    public GridPane cards;

    public TextField chooseText;

    public Label choosePlayerLabel;

    public ChoiceBox chooseFirstPlayer;

    public Label waitLabel;

    public Button chooseFirstPlayerButton;

    //get players from event on LoadButton click
    private ArrayList<String> players= new ArrayList<>();
    private ArrayList<String> opponents= new ArrayList<>();



    private int numSelectedCards=0;
    private boolean isChallenger;

    private ArrayList<String> cardDeck = new ArrayList<>();
    private String chosenCard=null;
    private ArrayList<String> chosenCardsChallenger = new ArrayList<>();


    private int numPlayers;

    public ChooseCardController(){}


    public void setIsChallenger(boolean isChallenger){
        this.isChallenger=isChallenger;
    }

    public void initChoiceBox(){
        chooseFirstPlayer.setItems(FXCollections.observableArrayList(opponents));
    }

    public void setOpponents(ArrayList<String > players){
        opponents.addAll(players);
    }

    public void initChosenCardsChallenger(ArrayList<String> cards){
        chosenCardsChallenger.addAll(cards);
    }

    private void initCardDeck(){
        cardDeck.add("Apollo");
        cardDeck.add("Artemis");
        cardDeck.add("Athena");
        cardDeck.add("Atlas");
        cardDeck.add("Demeter");
        cardDeck.add("Hephaestus");
        cardDeck.add("Minotaur");
        cardDeck.add("Pan");
        cardDeck.add("Prometheus");
    }

    public void initCardDeck(ArrayList<String> cards){
        cardDeck.addAll(cards);
    }

    private void initPlayers(ArrayList<String> player){
        players.addAll(player);
    }

    public void setNumPlayers(int n){
        this.numPlayers=n;
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    private ImageView image(String name){
        ImageView imageView = new ImageView("textures/"+name+".png");
        imageView.setFitHeight(150);
        imageView.setFitWidth(100);
        return imageView;
    }



    public void initGridChallenger(){
        for(int i=0;i<cardDeck.size();i++)
            cards.add(image(cardDeck.get(i)),i,0);
    }

    private void addGridEventChallenger() {
        cards.getChildren().forEach(item -> {
            item.setOnMouseClicked( event -> {
                if (event.getClickCount() == 1) {
                    if(numSelectedCards<numPlayers) {
                        Node node = (Node) event.getSource();
                        int n = GridPane.getColumnIndex(node);
                        String choice = cardDeck.get(n);
                        chosenCardsChallenger.add(choice);
                        updateText();
                        numSelectedCards++;
                    }
                }
            });

        });
    }

    private void updateText(){
        String text= "Your choices are: ";
        for(int i=0;i<chosenCardsChallenger.size();i++){
            text=text+chosenCardsChallenger.get(i)+", ";
        }
        chooseText.setText(text);
    }


    private void initGridNotChallenger(ArrayList<String> deck){
        for(int i=0;i<cardDeck.size();i++){
            if(deck.contains(cardDeck.get(i)))
                cards.add(image(cardDeck.get(i)),i,0);
        }
    }



    private void addGridEventNotChallenger() {
        cards.getChildren().forEach(item -> {
            item.setOnMouseClicked( event -> {
                if (event.getClickCount() == 1) {
                    Node node = (Node) event.getSource();
                    int n = GridPane.getColumnIndex(node);
                    chosenCard=cardDeck.get(n);
                    chooseText.setText("Your choice is: "+chosenCard+"!");
                }
            });

        });
    }





    public Scene chooseCardScene() throws IOException {
        Parent page = FXMLLoader.load(getClass().getResource("/fxml/chooseCard.fxml"));
        Scene scene = new Scene(page);
        return scene;
    }

    private void hideCards(){
        chooseText.setVisible(false);
        title.setVisible(false);
        startButton.setVisible(false);
        cards.setVisible(false);
        startButton.setVisible(false);
        buttonImage.setVisible(false);
    }

    //challenger
    public void waitChooseCards(){
        hideCards();
        waitLabel.setText("WAIT FOR OTHER PLAYERS");
        waitLabel.setVisible(true);
    }

    //not challenger
    public void waitForChallenger(){
        hideCards();
        waitLabel.setText("WAIT FOR THE CHALLENGER");
        waitLabel.setVisible(true);
    }

    //challenger
    public void showChooseFirstPlayer(){
        waitLabel.setVisible(false);
        chooseFirstPlayerButton.setVisible(true);
        choosePlayerLabel.setVisible(true);
        chooseFirstPlayer.setVisible(true);
    }

    public void loadCards(ArrayList<String> allCards) {
        chooseText.setVisible(true);
        chooseText.setMouseTransparent(true);
        title.setVisible(true);
        cards.setVisible(true);
        startButton.setVisible(true);
        buttonImage.setVisible(true);

        initCardDeck(allCards);

        //FOR CHALLENGER PLAYER
        if(isChallenger)
        {
            int numPlayers=getNumPlayers();
            title.setText("CHOOSE "+numPlayers+" CARDS!");
            if(numPlayers==3)
            {
                initChoiceBox();

            }

            initGridChallenger();
            addGridEventChallenger();
        }
        ///////////////////////


        //FOR NOT CHALLENGER PLAYERS
        if(!isChallenger)
        {
            initGridNotChallenger(chosenCardsChallenger);
            addGridEventNotChallenger();
        }
        ////////////////////////////



    }

    private void checkValidStartChallenger(){
        if(numSelectedCards==numPlayers && numPlayers==2) {
                //ready to play
        }
        else if(numSelectedCards==numPlayers && numPlayers==3){
            if (chooseFirstPlayer.getValue().toString()!=null) {
                //emitEvent firstPlayer
            }else
                alert("You must select one player!");
        }else
            alert("Check if you have selected cards and one player!");

    }

    private void checkValidStart(){
        if(chosenCard==null)
            alert("Choose a Card!");
        else{
//            for(int i=0;i<Manager.getInstance().getNumPlayers();i++)
//                Manager.getInstance().addCard(chosenCardsChallenger.get(i));
            //send chosen card
        }

    }


    public void sendCards(ActionEvent actionEvent){
        if(isChallenger)
            checkValidStartChallenger();
        else
            checkValidStart();
    }





    public void alert(String message){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }

    private String getFirstPlayer(){
        return chooseFirstPlayer.getValue().toString();
    }

    public void sendFirstPlayer(ActionEvent actionEvent) {
        //emitEvent first Player
    }


    public void launchGame(){
        Game game = new Game();
        for(int i=0;i<Manager.getInstance().getNumPlayers();i++)
            Manager.getInstance().addCard(chosenCardsChallenger.get(i));
        Stage stage = new Stage();
        stage.setScene(game.gameScene());
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.show();
//        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


}
