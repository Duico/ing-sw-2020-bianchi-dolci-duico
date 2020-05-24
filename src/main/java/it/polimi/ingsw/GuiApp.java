package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.gui.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class GuiApp extends Application {


    @Override
    public void start(Stage stage) {
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345);

        SceneManager sceneManager = new SceneManager(stage, clientConnection);
        sceneManager.handleEvent(new SceneEvent(SceneEvent.SceneType.LOGIN));

        clientConnection.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
