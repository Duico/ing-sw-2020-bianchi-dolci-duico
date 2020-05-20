package it.polimi.ingsw.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {


    public TextField textfield;
    public CheckBox twoPlayers;
    public CheckBox threePlayers;
    public Button start;


    public LoginController(){}


    private int checkValidStart(){
        //check if username is ok
        if(textfield.getText().length()!=0){    //esempio
            if(twoPlayers.isSelected() && !threePlayers.isSelected()){
                Manager.getInstance().setNumPlayers(2);
                Manager.getInstance().setUsername(textfield.getText());
                Manager.getInstance().addPlayer(textfield.getText());
                return 1;
            }else if(!twoPlayers.isSelected() && threePlayers.isSelected()){
                Manager.getInstance().setNumPlayers(3);
                Manager.getInstance().setUsername(textfield.getText());
                Manager.getInstance().addPlayer(textfield.getText());
                return 1;
            }
            else if(twoPlayers.isSelected() && threePlayers.isSelected())
            {
                return 2;
            }
            else
            {
                return 3;
            }
        }else
        {
            return 4;
        }
    }




    public Scene loginScene() throws IOException {
        Parent page = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(page);
        return scene;
    }



    public void startGame(ActionEvent actionEvent) throws Exception {
        if(checkValidStart()==1)
        {
            ChooseCardController chooseCard = new ChooseCardController();
            Stage stage = new Stage();
            stage.setScene(chooseCard.chooseCardScene());
            stage.setResizable(false);
            stage.sizeToScene();
            stage.show();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }
        else if(checkValidStart()==2){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("You can select only one option!");
            alert.showAndWait();
        }
        else if(checkValidStart()==3){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("You must select one option!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Invalid Username!");
            alert.showAndWait();
        }
    }


}
