package it.polimi.ingsw.client.gui;

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

public class ChooseCardController {


    @FXML
    public Button loadButton;

    public Label title;

    public Label loadTitle;

    public ImageView buttonImage;

    public Button startButton;

    public GridPane cards;

    public TextField chooseText;

    public Label choosePlayerLabel;

    public CheckBox choosePlayer1;

    public CheckBox choosePlayer2;

    //get players from event on LoadButton click
    private ArrayList<String> players= new ArrayList<>();
    private String firstPlayer;



    private int numSelectedCards=0;
    private boolean isChallenger=true;

    private ArrayList<String> cardDeck = new ArrayList<>();
    private String chosenCard=null;
    private ArrayList<String> chosenCardsChallenger = new ArrayList<>();


    private int numPlayers=Manager.getInstance().getNumPlayers();

    public ChooseCardController(){}



    private void setFirstPlayer(String name){
        this.firstPlayer=name;
    }


    private void initChosenCardsChallenger(){
        chosenCardsChallenger.add("Hephaestus");
        chosenCardsChallenger.add("Pan");
        chosenCardsChallenger.add("Artemis");
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

    private void initPlayers(){
        players.add(Manager.getInstance().getUsername());
        players.add("player1");
        Manager.getInstance().addPlayer("player1");
        players.add("player2");
        Manager.getInstance().addPlayer("player2");
    }




    private ImageView image(String name){
        ImageView imageView = new ImageView("textures/"+name+".png");
        imageView.setFitHeight(150);
        imageView.setFitWidth(100);
        return imageView;
    }



    private void initGridChallenger(){
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

    public void loadCards(ActionEvent actionEvent) {

        loadTitle.setVisible(false);
        loadButton.setVisible(false);
        chooseText.setVisible(true);
        chooseText.setMouseTransparent(true);
        title.setVisible(true);
        cards.setVisible(true);
        startButton.setVisible(true);
        buttonImage.setVisible(true);

        initCardDeck();

        //FOR CHALLENGER PLAYER
        if(isChallenger)
        {
            initPlayers();
            title.setText("CHOOSE "+Manager.getInstance().getNumPlayers()+" CARDS!");
            if(Manager.getInstance().getNumPlayers()==3)
            {
                choosePlayerLabel.setVisible(true);
                choosePlayer1.setVisible(true);
                //sto ipotizzando che l'username del current player sia inserito in posizione 0 di players
                choosePlayer1.setText(players.get(1));
                choosePlayer2.setVisible(true);
                choosePlayer2.setText(players.get(2));

            }

            initGridChallenger();
            addGridEventChallenger();
        }
        ///////////////////////


        //FOR NOT CHALLENGER PLAYERS
        if(!isChallenger)
        {
            initPlayers();
            initChosenCardsChallenger();
            initGridNotChallenger(chosenCardsChallenger);
            addGridEventNotChallenger();
        }
        ////////////////////////////



    }

    private int checkValidStartChallenger(){
        if(numSelectedCards==numPlayers && numPlayers==2)
            return 1;
        else if(numSelectedCards==numPlayers && numPlayers==3){
            if (choosePlayer1.isSelected() && !choosePlayer2.isSelected()) {
                setFirstPlayer(choosePlayer1.getText());
                return 1;
            } else if (!choosePlayer1.isSelected() && choosePlayer2.isSelected()){
                setFirstPlayer(choosePlayer2.getText());
                return 1;
            }
            else if(choosePlayer1.isSelected() && choosePlayer2.isSelected())
                return 2;
            else
                return 3;
        }else
            return 4;

    }

    private boolean checkValidStart(){
        if(chosenCard==null)
            return false;
        return true;
    }

    public void startGame(ActionEvent actionEvent) throws Exception {
        //challenger needs to get chosen card from event before start
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if(isChallenger) {
            switch (checkValidStartChallenger()){
                case 1:
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
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                    break;
                case 2:
                    alert.setHeaderText("You can select only one first player!");
                    alert.showAndWait();
                    break;
                case 3:
                    alert.setHeaderText("You must select one player!");
                    alert.showAndWait();
                    break;
                case 4:
                    alert.setHeaderText("Check if you have selected cards and one player!");
                    alert.showAndWait();
                    break;
            }
        }else
        {
            if(checkValidStart()){
                for(int i=0;i<Manager.getInstance().getNumPlayers();i++)
                    Manager.getInstance().addCard(chosenCardsChallenger.get(i));
                    Manager.getInstance().setChosenCard(chosenCard);
                    Game game = new Game();
                    Stage stage = new Stage();
                    stage.setScene(game.gameScene());
                    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX(primaryScreenBounds.getMinX());
                    stage.setY(primaryScreenBounds.getMinY());
                    stage.setWidth(primaryScreenBounds.getWidth());
                    stage.setHeight(primaryScreenBounds.getHeight());
                    stage.show();
                    ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
                }else
                {
                    alert.setHeaderText("Choose a Card!");
                    alert.showAndWait();
                }

        }


    }
}
