package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseCardController implements GuiEventEmitter {

    private GuiEventListener listener;

    @FXML
    public Label header;

    public ImageView buttonImage;

    public Button startButton;

    public HBox cards;

    public Label chooseText;

    public Label choosePlayerLabel;

    public ChoiceBox firstPlayerChoiceBox;

    public VBox playerBox;

    public VBox waitBox;

    public Label waitLabel;
    @FXML
    private VBox cardsBox;

    private List<String> opponents= new ArrayList<>();

    private Integer requiredNumCards;
    private int numSelectedCards=0;
    private boolean isChallenger;

//    private List<String> cardDeck = new ArrayList<>();
    private List<String> cardDeck;
    private String chosenCard=null;
    private List<String> chosenCardsChallenger = new ArrayList<>();
    private List<String> chosenCards = new ArrayList<>();

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

    public void askNumCards(Integer requiredNumCards){
        if(requiredNumCards>1){
            initGrid(cardDeck);
        }else{
            initGrid(chosenCards);
        }
    }

    public void initGrid(List<String> cards){
        for(int i=0;i<cards.size();i++){
            ImageView image = image(cards.get(i));
            addEventCard(image, cards.get(i));
            this.cards.getChildren().add(image);
        }
    }

    private void addEventCard(Node node, String name){
        node.setOnMouseClicked(event->{

            if(chosenCards.contains(name))
            {
                System.out.println("remove"+name);
                chosenCards.remove(name);
//                node.getStyleClass().removeAll("card_selected");
            }else{
                if(chosenCards.size()<requiredNumCards)
                {
                    System.out.println("add"+name);
                    chosenCards.add(name);
                    //ADD TEXT TO CHOSEN CARD
//                    node.getStyleClass().addAll("card_selected");
                }
            }

//            chooseText.setText("Your choice is: "+chosenCard+"!");
        });
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

//    //TODO remove
//    public void initGridChallenger(List<String> chosenCards){
//        List<String> cardstmp = new LinkedList<String>();
//        cardstmp.add("Athena");
//        cardstmp.add("Prometheus");
//        cardstmp.add("Atlas");
//        initGridNotChallenger(cardstmp);
//    }

    public void initGridChallenger(){
        for(int i=0;i<cardDeck.size();i++){
            ImageView image = image(cardDeck.get(i));
            HBox.setMargin(image, new Insets(0, (i==cardDeck.size()-1)?0:10, 0, i==0?0:10));
            cards.getChildren().add(image);
        }
    }


    //Events must be added on the single ImageView
    public void addGridEventChallenger() {
        cards.getChildren().forEach(item -> {
            item.setOnMouseClicked( event -> {
                if (event.getClickCount() == 1) {
                    if(numSelectedCards<numPlayers) {
                        Node node = (Node) event.getSource();
//                        int n = GridPane.getColumnIndex(node);
                        int i;
                       for(i=0;i<cards.getChildren().size();i++){
                           if(node.equals(cards.getChildren().get(i)))
                               break;
                       }
                        String choice = cardDeck.get(i);
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
//                          int n = GridPane.getColumnIndex(node);
                          int i;
                          for(i=0;i<cards.getChildren().size();i++){
                              if(node.equals(cards.getChildren().get(i)))
                                  break;
                          }
                          chosenCard=chosenCardsChallenger.get(i);
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
            waitLabel.setText("Wait for the other players");
            waitBox.setVisible(true);
        });
    }

    //not challenger
    public void waitForChallenger(){
        Platform.runLater(()->{
            hideCards();
            waitLabel.setText("Wait for the challenger");
            waitBox.setVisible(true);
        });
    }

    //challenger
    public void showChooseFirstPlayer(List<String> usernames){
        setOpponents(usernames);
        Platform.runLater(()->{
            waitBox.setVisible(false);
            playerBox.setVisible(true);
            initChoiceBox();
        });
    }

    public void loadCards(List<String> cardDeck) {
        Platform.runLater(()->{
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

//    private void checkValidStartChallenger(){
//
//        if(numSelectedCards==numPlayers) {
//            emitChallengerCards(chosenCardsChallenger);
//            waitChooseCards();
//        }else
//            alert("Check if you have selected "+numPlayers+" cards!");
//
//    }
//
//    private void checkValidStart(){
//        if(chosenCard==null)
//            alert("Choose a Card!");
//        else{
//            emitChosenCard(chosenCard);
//        }
//    }


//    public void sendCards(ActionEvent actionEvent){
//        if(isChallenger)
//            checkValidStartChallenger();
//        else
//            checkValidStart();
//    }

    public void sendCards(ActionEvent actionEvent){
        checkValidStart();
    }

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


    private void emitChosenCard(String chosenCard){
        listener.onChooseCard(chosenCard);
    }

    private void emitChallengerCards(List<String> challengerCards){
        listener.onChallengeCards(challengerCards);
    }

    private void emitFirstPlayer(String firstPlayer){
        listener.onFirstPlayer(firstPlayer);
    }

    @Override
    public void setEventListener(GuiEventListener listener) {
        this.listener = listener;
    }
}
