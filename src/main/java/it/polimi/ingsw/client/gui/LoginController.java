package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.client.message.SignUpMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController extends ClientEventEmitter {


    @FXML
    public TextField textfield;
    @FXML
    public Button start;
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public Label message;
    @FXML
    public Label waitLabel;
    @FXML
    public Pane loginPane;

    private int numPlayers;
    private String username;


    public void setMessage(String message){
        this.message.setText(message);
    }

    public void setNumPlayers(int n){
        this.numPlayers= n;
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public void setUsername(String name){
        this.username=name;
    }

    public String getUsername(){
        return this.username;
    }


    public LoginController(){
    }

    @FXML
    protected void initialize() {
        initChoiceBox();
        setVisibleChoiceBox(true);
        waitLabel.setStyle("-fx-background-color:white;");
    }



    public void setVisibleChoiceBox(boolean set){
        choiceBox.setVisible(set);
    }

    public void askSetUpInfo(boolean askNumPlayers){
        if(askNumPlayers){
            setMessage("Select number of players:");
            setVisibleChoiceBox(true);
        }else{
            setMessage("Wait for the Challenger");
            setVisibleChoiceBox(false);
        }
    }

    public void correctSignUp(boolean waitOtherPlayers){
        if(waitOtherPlayers){
            loginPane.setVisible(false);
            waitLabel.setVisible(true);
        }
    }

    public void initChoiceBox(){
        String twoPlayers="2 PLAYERS";
        String threePlayers="3 PLAYERS";
        choiceBox.setItems(FXCollections.observableArrayList(twoPlayers,threePlayers));
    }

    private int checkValidStart(){
        //check if username is ok
        String insert = textfield.getText().trim();
        if(insert.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){    //esempio
            if(choiceBox.getValue().toString().equals("2 PLAYERS")){
                System.out.println("2");
                Manager.getInstance().setNumPlayers(2);
//                Manager.getInstance().setUsername(textfield.getText());
//                Manager.getInstance().addPlayer(textfield.getText());
                return 1;
            }else if(choiceBox.getValue().toString().equals("3 PLAYERS")){
                System.out.println("3");
                Manager.getInstance().setNumPlayers(3);
//                Manager.getInstance().setUsername(textfield.getText());
//                Manager.getInstance().addPlayer(textfield.getText());
                return 1;
            }else
                return 2;
        }
        return 3;
    }



//    public Scene loginScene() throws IOException {
//        Parent page = FXMLLoader.load(getClass().getResource("/login.fxml"));
//        Scene scene = new Scene(page);
//        return scene;
//    }

    public void launchChooseCard() throws IOException {
        ChooseCardController chooseCard = new ChooseCardController();
        Stage stage = new Stage();
        stage.setScene(chooseCard.chooseCardScene());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
//        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }



    public void startGame(ActionEvent actionEvent) throws Exception {

        if(checkValidStart()==1)
        {
            emitSignUp(new SignUpMessage(username, numPlayers));
        }
        else if(checkValidStart()==2){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("You must one option!");
            alert.showAndWait();
        }
        else if(checkValidStart()==3){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid Username!");
            alert.showAndWait();
        }


    }


}
