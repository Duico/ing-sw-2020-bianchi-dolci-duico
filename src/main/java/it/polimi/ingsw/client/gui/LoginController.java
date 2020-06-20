package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginController implements GuiEventEmitter {

    public GuiEventListener listener;

    @FXML
    public TextField textfield;
    @FXML
    public Button start;
    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public VBox numPlayersVBox;
    @FXML
    public VBox waitLabel;
    @FXML
    public VBox loginPane;
    @FXML
    private VBox persistencyVbox;
    @FXML
    private CheckBox persistencyCheckBox;



//    public void setMessage(String message){
//        this.message.setText(message);
//    }



    public LoginController(){
    }

    @FXML
    protected void initialize() {
        initChoiceBox();
    }



    public void setVisibleChoiceBox(boolean set){
        choiceBox.setVisible(set);
    }

    public void askSetUpInfo(boolean askNumPlayers, boolean askPersistency){
        Platform.runLater(()->{
            loginPane.setVisible(true);
            if(askNumPlayers){
//                setMessage("Select number of players:");
                persistencyVbox.setVisible(true);
                if (askPersistency) {
                    persistencyVbox.setDisable(false);
                }
                numPlayersVBox.setVisible(true);
            }else{
//                setVisibleChoiceBox(false);
            }
        });
    }

    public void correctSignUp(){
        loginPane.setVisible(false);
        waitLabel.setVisible(true);
    }

    public void initChoiceBox(){
        String twoPlayers="2 PLAYERS";
        String threePlayers="3 PLAYERS";
        choiceBox.setItems(FXCollections.observableArrayList(twoPlayers,threePlayers));
        choiceBox.setValue(twoPlayers);
    }

    private Integer getNumPlayers(){
        if(choiceBox.getValue()!=null){
            if(choiceBox.getValue().equals("2 PLAYERS")){
                return 2;
            }else if(choiceBox.getValue().equals("3 PLAYERS")){
                return 3;
            }else{
                alert("You must select one option!");
                return null;
            }
        }else
            return null;

    }

    public void startGame(ActionEvent actionEvent) throws Exception {
        String username = textfield.getText().trim();
        boolean checkUsername = username.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$");
        if(checkUsername) {
            if(choiceBox.isVisible()){
                Integer choiceBoxNumPlayers = getNumPlayers();
                if(choiceBoxNumPlayers==null){
                    textfield.setText("");
                    alert("Number of players not set!");
                    return;
                }
                boolean persistency = persistencyCheckBox.isSelected();
                emitLogin(username, choiceBoxNumPlayers, persistency);

            }else{
                emitLogin(username);
            }

        }
        else{
            alert("Invalid username!");
        }
    }

    public void alert(String message){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }


    private void emitLogin(String username, Integer numPlayers, boolean persistency){
        listener.onLogin(username, numPlayers, persistency);
    }
    private void emitLogin(String username){
        listener.onLogin(username, null, false);
    }

    @Override
    public void setEventListener(GuiEventListener listener) {
        this.listener = listener;
    }
}
