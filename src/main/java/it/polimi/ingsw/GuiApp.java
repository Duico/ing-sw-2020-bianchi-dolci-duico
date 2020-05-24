package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.event.MessageListener;
import it.polimi.ingsw.client.gui.*;
import it.polimi.ingsw.client.message.SignUpListener;
import javafx.application.Application;
import javafx.stage.Stage;

public class GuiApp extends Application {


    @Override
    public void start(Stage stage) {
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345);
        clientConnection.run();

        SceneManager sceneManager = new SceneManager(stage, clientConnection);
        sceneManager.handleEvent(new SceneEvent(SceneEvent.SceneType.LOGIN));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
