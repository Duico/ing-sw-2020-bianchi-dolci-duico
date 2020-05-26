package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.event.CardViewEvent;
import it.polimi.ingsw.view.event.ChallengerCardViewEvent;
import it.polimi.ingsw.view.event.FirstPlayerViewEvent;
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
import java.util.List;

public class ChooseCardController extends ClientEventEmitter {

    @FXML
    public Label title;

    public ImageView buttonImage;

    public Button startButton;

    public GridPane cards;

    public TextField chooseText;

    public Label choosePlayerLabel;

    public ChoiceBox firstPlayerChoiceBox;

    public Label waitLabel;

    public Button chooseFirstPlayerButton;

    //get players from event on LoadButton click
    private List<String> players= new ArrayList<>();
    private ArrayList<String> opponents= new ArrayList<>();

    private boolean isChallengerCardsLoaded=false;

    private int numSelectedCards=0;
    private boolean isChallenger;

//    private List<String> cardDeck = new ArrayList<>();
    private List<String> cardDeck = GuiModel.getInstance().getCardDeck();
    private String chosenCard=null;
    private List<String> chosenCardsChallenger = new ArrayList<>();


    private int numPlayers=GuiModel.getInstance().getNumPlayers();

    public ChooseCardController(){}

    public void setIsChallenger(boolean isChallenger){
        this.isChallenger=isChallenger;
    }


    public void initChoiceBox(){
        System.out.println("init choice box");
//        Platform.runLater(()->{
            firstPlayerChoiceBox.setItems(FXCollections.observableArrayList(opponents));
//        });
    }

    public void setOpponents(){
        for(Player player:GuiModel.getInstance().getPlayers())
        {
            if(!player.getNickName().equals(GuiModel.getInstance().getCurrentUsername()) && !opponents.contains(player.getNickName()))
                opponents.add(player.getNickName());
        }
    }

    public void initChosenCardsChallenger(List<String> cards){
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

    public void initCardDeck(List<String> cards){
        cardDeck.addAll(cards);
    }

    private void initPlayers(List<String> player){
        players.addAll(player);
    }

    private void setNumPlayers(int n){
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


    public void initGridNotChallenger(List<String> chosenCards){
          System.out.println("init grid not challenger");
          for(int i=0;i<cardDeck.size();i++){
              if(chosenCards.contains(cardDeck.get(i)))
                  cards.add(image(cardDeck.get(i)),i,0);
          }
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
        title.setVisible(false);
        startButton.setVisible(false);
        cards.setVisible(false);
        startButton.setVisible(false);
        buttonImage.setVisible(false);
    }

    //challenger
    public void waitChooseCards(){
        hideCards();
//        Platform.runLater(()->{
            waitLabel.setText("WAIT FOR OTHER PLAYERS");
//        });
        waitLabel.setVisible(true);
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
    public void showChooseFirstPlayer(){
        waitLabel.setVisible(false);
        chooseFirstPlayerButton.setVisible(true);
        choosePlayerLabel.setVisible(true);
        firstPlayerChoiceBox.setVisible(true);
        setOpponents();
        initChoiceBox();
    }

    public void loadCards() {
            chooseText.setVisible(true);
            chooseText.setMouseTransparent(true);
            title.setVisible(true);
            cards.setVisible(true);
            startButton.setVisible(true);
            buttonImage.setVisible(true);



        //FOR CHALLENGER PLAYER
        if(isChallenger)
        {
            setNumPlayers(GuiModel.getInstance().getNumPlayers());
            int numPlayers=getNumPlayers();
            title.setText("CHOOSE "+numPlayers+" CARDS!");

            initGridChallenger();
            addGridEventChallenger();
        }
        ///////////////////////

        //FOR NOT CHALLENGER PLAYERS
        else {
            initGridNotChallenger(chosenCardsChallenger);
            addGridEventNotChallenger();
        }
        ////////////////////////////



    }

    private void checkValidStartChallenger(){
        if(numSelectedCards==numPlayers && numPlayers==2) {
            emitViewEvent(new ChallengerCardViewEvent(chosenCardsChallenger));
            waitChooseCards();
        }
        else if(numSelectedCards==numPlayers && numPlayers==3){
//            if (firstPlayerChoiceBox.getValue().toString()!=null) {
                emitViewEvent(new ChallengerCardViewEvent(chosenCardsChallenger));
                waitChooseCards();
        }else
            alert("Check if you have selected "+numPlayers+" cards!");

    }

    private void checkValidStart(){
        if(chosenCard==null)
            alert("Choose a Card!");
        else{
            GuiModel.getInstance().setCurrentCard(chosenCard);
            emitViewEvent(new CardViewEvent(chosenCard));
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
        //emitEvent first Player
        for(Player p: GuiModel.getInstance().getPlayers()){
            if(p.getNickName().equals(getFirstPlayer()))
                emitViewEvent(new FirstPlayerViewEvent(p));
        }
    }


    public void launchGame(){
        MainController mainController = new MainController();
        for(int i = 0; i< GuiModel.getInstance().getNumPlayers(); i++)
            GuiModel.getInstance().addCard(chosenCardsChallenger.get(i));
        Stage stage = new Stage();
        stage.setScene(mainController.gameScene());
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.show();
//        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


}
