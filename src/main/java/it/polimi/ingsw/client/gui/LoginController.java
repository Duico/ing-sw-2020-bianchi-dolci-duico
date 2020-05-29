package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.event.MouseEvent;

public class LoginController implements GuiEventEmitter {

    public GuiEventListener listener;

    @FXML
    public TextField textfield;
    @FXML
    public Button start;
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public Label message;
    @FXML
    public VBox waitLabel;
    @FXML
    public VBox loginPane;

    private int numPlayers;
    private String username;



//    public void setMessage(String message){
//        this.message.setText(message);
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
        Platform.runLater(()->{
            if(askNumPlayers){
//                setMessage("Select number of players:");
                message.setVisible(true);
                setVisibleChoiceBox(true);
            }else{
                setVisibleChoiceBox(false);
            }
        });
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

    private Integer getNumPlayers(){
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
        String username = textfield.getText().trim();
        boolean checkUsername = username.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$");
        if(checkUsername) {
            if(choiceBox.isVisible()){
                Integer choiceBoxNumPlayers = getNumPlayers();
                if(choiceBoxNumPlayers==null){
                    textfield.setText("");
                    alert("Number of players not set!");
                }
                else {
                    emitLogin(username, choiceBoxNumPlayers);
                }
            }else{
                emitLogin(username, null);
            }

        }
        else{
            alert("Invalid username!");
        }

    }

    public void startGame(ActionEvent actionEvent) throws Exception {
        checkValidStart();
    }

    public void alert(String message){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }


    public void emitLogin(String username, Integer numPlayers){
        listener.onLogin(username, numPlayers);
    }

    @Override
    public void setEventListener(GuiEventListener listener) {
        this.listener = listener;
    }
}
