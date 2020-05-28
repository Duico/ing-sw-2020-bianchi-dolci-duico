package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChooseCardController extends GuiEventEmitter {

    @FXML
    public Label header;

    public ImageView buttonImage;

    public Button startButton;

    public HBox cards;

    public Label chooseText;

    public Label choosePlayerLabel;

    public ChoiceBox firstPlayerChoiceBox;

    public Label waitLabel;

    public Button chooseFirstPlayerButton;

    //get players from event on LoadButton click
//    private List<String> players= new ArrayList<>();
    private List<String> opponents= new ArrayList<>();


    private int numSelectedCards=0;
    private boolean isChallenger;

//    private List<String> cardDeck = new ArrayList<>();
    private List<String> cardDeck;
    private String chosenCard=null;
    private List<String> chosenCardsChallenger = new ArrayList<>();

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

    public void initChoiceBox(){
//        System.out.println("init choice box");
            firstPlayerChoiceBox.setItems(FXCollections.observableArrayList(opponents));
    }

    public void setOpponents(List<String> opponents){
        this.opponents=opponents;
    }

    public void initChosenCardsChallenger(List<String> cards){
        chosenCardsChallenger.addAll(cards);
    }



    private ImageView image(String name){
        ImageView imageView = new ImageView("textures/"+name+".png");
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(141);
        imageView.setFitWidth(84);
        imageView.getStyleClass().addAll("card_image");
        return imageView;
    }


    public void initGridNotChallenger(List<String> chosenCards){
          for(int i=0;i<cardDeck.size();i++){
              if(chosenCards.contains(cardDeck.get(i))){
                  ImageView image = image(cardDeck.get(i));
                  HBox.setMargin(image, new Insets(0, (i==cardDeck.size()-1)?0:10, 0, i==0?0:10));
                  cards.getChildren().addAll(image);
              }
          }
    }

    //TODO remove
    public void initGridChallenger(List<String> chosenCards){
        List<String> cardstmp = new LinkedList<String>();
        cardstmp.add("Athena");
        cardstmp.add("Prometheus");
        cardstmp.add("Atlas");
        initGridNotChallenger(cardstmp);
    }


    //Events must be added on the single ImageView
    public void addGridEventChallenger() {
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
        chooseText.setText(getUpdateText());
    }

    private String getUpdateText(){
        String text= "Your choices are: ";
        for(int i=0;i<chosenCardsChallenger.size();i++){
            text=text+chosenCardsChallenger.get(i)+", ";
        }
        return text;
    }





    public void addGridEventNotChallenger() {

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

    public void hideCards(){
            chooseText.setVisible(false);
            header.setVisible(false);
            startButton.setVisible(false);
            cards.setVisible(false);
            startButton.setVisible(false);
            buttonImage.setVisible(false);
    }

    //challenger
    public void waitChooseCards(){
        Platform.runLater(()->{
            hideCards();
            waitLabel.setText("WAIT FOR OTHER PLAYERS");
            waitLabel.setVisible(true);
        });
    }

    //not challenger
    public void waitForChallenger(){
        hideCards();
        Platform.runLater(()->{
            waitLabel.setText("WAIT FOR THE CHALLENGER");
        });
        waitLabel.setVisible(true);
    }

    //challenger
    public void showChooseFirstPlayer(List<String> usernames){
        Platform.runLater(()->{
            waitLabel.setVisible(false);
            chooseFirstPlayerButton.setVisible(true);
            choosePlayerLabel.setVisible(true);
            firstPlayerChoiceBox.setVisible(true);
            setOpponents(usernames);
            initChoiceBox();
        });
    }

    public void loadCards() {
        Platform.runLater(()->{
            chooseText.setVisible(true);
            chooseText.setMouseTransparent(true);
            header.setVisible(true);
            cards.setVisible(true);
            startButton.setVisible(true);
            buttonImage.setVisible(true);



            //FOR CHALLENGER PLAYER
            if(isChallenger)
            {
//            setNumPlayers(GuiModel.getInstance().getNumPlayers());

                header.setText("Chose "+numPlayers+" cards");

                initGridChallenger(cardDeck);
//                addGridEventChallenger();
            }
            ///////////////////////

            //FOR NOT CHALLENGER PLAYERS
            else {
                initGridNotChallenger(chosenCardsChallenger);
//                addGridEventNotChallenger();
            }
            ////////////////////////////


        });
    }

    private void checkValidStartChallenger(){
        if(numSelectedCards==numPlayers && numPlayers==2) {
            emitChallengerCards(chosenCardsChallenger);
            waitChooseCards();
        }
        else if(numSelectedCards==numPlayers && numPlayers==3){
            emitChallengerCards(chosenCardsChallenger);
            waitChooseCards();
        }else
            alert("Check if you have selected "+numPlayers+" cards!");

    }

    private void checkValidStart(){
        if(chosenCard==null)
            alert("Choose a Card!");
        else{
            emitChosenCard(chosenCard);
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
        return firstPlayerChoiceBox.getValue().toString();
    }

    public void sendFirstPlayer(ActionEvent actionEvent) {
        emitFirstPlayer(getFirstPlayer());
    }


//    public void launchGame(){
//        MainController mainController = new MainController();
//        for(int i = 0; i< GuiModel.getInstance().getNumPlayers(); i++)
//            GuiModel.getInstance().addCard(chosenCardsChallenger.get(i));
//        Stage stage = new Stage();
//        stage.setScene(mainController.gameScene());
//        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//        stage.setX(primaryScreenBounds.getMinX());
//        stage.setY(primaryScreenBounds.getMinY());
//        stage.setWidth(primaryScreenBounds.getWidth());
//        stage.setHeight(primaryScreenBounds.getHeight());
//        stage.show();
////        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
//    }
//

}
