package it.polimi.ingsw.client.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private boolean isVisibleChoiceBox;

    public TextField textfield;
    public Button start;
    public ChoiceBox choiceBox;


    public LoginController(){
    }

    @FXML
    protected void initialize() {
           setVisibleChoiceBox();
    }

    public void onConnection(boolean askNumPlayers){
        if(askNumPlayers)
            setVisibleChoiceBox();
        //stampa
    }

    public void setVisibleChoiceBox(){
        choiceBox.setVisible(true);
    }


    public void initChoiceBox(){
        String twoPlayers="2 PLAYERS";
        String threePlayers="3 PLAYERS";
        choiceBox.setItems(FXCollections.observableArrayList(twoPlayers,threePlayers));
    }

    private int checkValidStart(){
        //check if username is ok
        if(textfield.getText().length()!=0){    //esempio
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


    public void setInvisibleLabels(){
        choiceBox.setVisible(false);
    }

    public void usernameAlreadyUsed(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("This usename is already used!");
        alert.showAndWait();
    }


    public Scene loginScene() throws IOException {
        Parent page = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(page);
        return scene;
    }

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
        System.out.println(choiceBox.getValue().toString());
        if(checkValidStart()==1)
        {
            //emitEvent(username, numPLayers)
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


    @FXML
    private void showNumPlayers(MouseEvent mouseEvent) {
        initChoiceBox();
    }

    public void setVisible(ActionEvent actionEvent) {
        if(this.isVisibleChoiceBox)
            setVisibleChoiceBox();
    }
    }
