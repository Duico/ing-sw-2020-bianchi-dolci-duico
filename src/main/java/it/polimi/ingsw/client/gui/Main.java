package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.GameMessageVisitor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        //or use .setLocation

        Parent login = fxmlLoader.load();
        Scene scene = new Scene(login);

        stage.setScene(scene);
        LoginController controller = fxmlLoader.getController();
//        login.setVisibleChoiceBox(false);
        //TEMP
        GameMessageVisitor guiLoginGameMessageVisitor = new GuiLoginGameMessageVisitor(new GuiModelEventVisitor(), new GuiControllerResponseVisitor(), new GuiLoginSetUpMessageVisitor(controller));
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345, guiLoginGameMessageVisitor);
        new Thread( clientConnection::start ).start();

        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();


        controller.setVisibleChoiceBox(false);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
