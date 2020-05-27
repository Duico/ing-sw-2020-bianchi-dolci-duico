package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import it.polimi.ingsw.client.message.SignUpMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController extends GuiEventEmitter {

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

    private Optional<Boolean> askNumPlayers = Optional.empty();
    private int numPlayers;
    private String username;

    public void setAskNumPlayers(boolean askNumPlayers){
        this.askNumPlayers = Optional.of(askNumPlayers);
    }

    public void setMessage(String message){
        this.message.setText(message);
    }
//
//    public void setNumPlayers(int n){
//        this.numPlayers= n;
//    }
//
//    public int getNumPlayers(){
//        return this.numPlayers;
//    }
//
//    public void setUsername(String name){
//        this.username=name;
//    }
//
//    public String getUsername(){
//        return this.username;
//    }


    public LoginController(){
    }

    @FXML
    protected void initialize() {
        initChoiceBox();
        setVisibleChoiceBox(true);
    }



    public void setVisibleChoiceBox(boolean set){
        choiceBox.setVisible(set);
    }

    public void askSetUpInfo(boolean askNumPlayers){
        if(askNumPlayers){
            setMessage("Select number of players:");
            setVisibleChoiceBox(true);
        }else{
            setVisibleChoiceBox(false);
        }
    }

    public void correctSignUp(boolean waitOtherPlayers){
        loginPane.setVisible(false);

//        if(waitOtherPlayers){
//            Platform.runLater(()->{
////                waitLabel.setText("Waiting for other Players");
//            });
//        }else {
//            Platform.runLater(()->{
////                waitLabel.setText("Waiting for other Players");
//
//            });
//        }

        waitLabel.setVisible(true);
    }

    public void initChoiceBox(){
        String twoPlayers="2 PLAYERS";
        String threePlayers="3 PLAYERS";
        choiceBox.setItems(FXCollections.observableArrayList(twoPlayers,threePlayers));
    }

    private Integer checkChoiceBox(){
        if(choiceBox.getValue()!=null){
            if(choiceBox.getValue().toString().equals("2 PLAYERS")){
                return 2;
            }else if(choiceBox.getValue().toString().equals("3 PLAYERS")){
                return 3;
            }else{
                alert("You must select one option!");
                return null;
            }
        }else
            return null;

    }

    private void checkValidStart(){
//        //check if username is ok
//        String insert = textfield.getText().trim();
//        boolean checkUsername = insert.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$");
//        if(askNumPlayers.equals(Optional.empty())){
//            return;
//        }
//        if(askNumPlayers.get()){
//            if(checkUsername) {
//                Integer choiceBoxNumPlayers = checkChoiceBox();
//                if(choiceBoxNumPlayers==null){
//                    username=null;
//                }
//                else {
//                    username = insert;
//                    if(choiceBoxNumPlayers==2){
////                        System.out.println("signupmessage"+" "+username+choiceBoxNumPlayers);
////                        GuiModel.getInstance().setNumPlayers(2);
//                        GuiModel.getInstance().setCurrentUsername(username);
//                        emitSignUp(new SignUpMessage(username, choiceBoxNumPlayers));
//                    }else if(choiceBoxNumPlayers==3){
////                        System.out.println("signupmessage"+" "+username+choiceBoxNumPlayers);
////                        GuiModel.getInstance().setNumPlayers(3);
//                        GuiModel.getInstance().setCurrentUsername(username);
//                        emitSignUp(new SignUpMessage(username, choiceBoxNumPlayers));
//                    }
//                }
//
//
//            }else{
//                alert("Invalid username!");
//            }
//
//        }else{
//            if(checkUsername){
//                username=insert;
////                System.out.println("signupmessage"+" "+username);
//                GuiModel.getInstance().setCurrentUsername(username);
//                emitSignUp(new SignUpMessage(username));
//
//            }else{
//                alert("Invalid username!");
//            }
//        }

    }


    public void alert(String message){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }


}
