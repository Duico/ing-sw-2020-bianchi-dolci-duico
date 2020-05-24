package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.client.message.SignUpListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private ChooseCardController getChooseCardController(){

    }

    @Override
    public void start(Stage stage) throws Exception {
        //or use .setLocation
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent login = loginLoader.load();
        Scene scene = new Scene(login);
        stage.setScene(scene);
        LoginController loginController = loginLoader.getController();

        FXMLLoader chooseCardLoader = new FXMLLoader(getClass().getResource("/fxml/chooseCard.fxml"));
        //TEMP
        GameMessageVisitor guiLoginGameMessageVisitor = new GuiLoginGameMessageVisitor(new GuiModelEventVisitor(), new GuiControllerResponseVisitor(), new GuiLoginSetUpMessageVisitor(loginController));
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345, guiLoginGameMessageVisitor);
        new Thread( clientConnection::start ).start();

        loginController.addEventListener(SignUpListener.class,clientConnection);
//        loginController.addEventListener();
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();


        loginController.setVisibleChoiceBox(false);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
