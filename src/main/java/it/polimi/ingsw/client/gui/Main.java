package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.event.MessageListener;
import it.polimi.ingsw.client.message.SignUpListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        //or use .setLocation
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent login = loginLoader.load();
        Scene scene = new Scene(login);
        stage.setScene(scene);
        LoginController loginController = loginLoader.getController();

        FXMLLoader chooseCardLoader = new FXMLLoader(getClass().getResource("/fxml/chooseCard.fxml"));
        Parent chooseCard = chooseCardLoader.load();
        Scene chooseCardScene = new Scene(chooseCard);
        ChooseCardController chooseCardController= chooseCardLoader.getController();

        MainController mainController= new MainController();


        //TEMP
        GuiMessageVisitor guiMessageVisitor = new GuiLoginGameMessageVisitor(loginController);
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345);
        clientConnection.run();
        clientConnection.addEventListener(MessageListener.class, guiMessageVisitor);
        loginController.addEventListener(SignUpListener.class,clientConnection);


        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();


        loginController.setVisibleChoiceBox(false);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
