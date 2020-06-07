package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.view.ViewEventListener;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneManager implements SceneEventListener {
    private final Stage stage;
    //private final ClientConnection clientConnection;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private SceneEvent.SceneType sceneType;
    private LoginController loginController;
    private ChooseCardController chooseCardController;
    private MainController mainController;
    //private EndController endController;

    public SceneManager(Stage stage){
        this.stage = stage;
        //this.clientConnection = clientConnection;
    }

    public SceneEventEmitter startGame(LoginController loginController, ChooseCardController chooseCardController, MainController mainController){
        this.loginController = loginController;
        this.chooseCardController = chooseCardController;
        this.mainController = mainController;

        SceneEventEmitter sceneEventEmitter = new SceneEventEmitter();
        sceneEventEmitter.addEventListener(SceneEventListener.class, this);

        this.sceneType= SceneEvent.SceneType.LOGIN;
        loadScene(SceneEvent.SceneType.LOGIN);

        return sceneEventEmitter;
    }

    @Override
    public void handleEvent(SceneEvent evt) {
        if(evt.getSceneType().equals(sceneType)){
            return;
        }
            Platform.runLater(()->{
                sceneType=evt.getSceneType();
                loadScene(evt.getSceneType());
            });
    }


    /*private void setConnectionListener(GuiMessageVisitor guiMessageVisitor){
        clientConnection.addEventListener(MessageListener.class, guiMessageVisitor);
    }*/

    private void loadScene(SceneEvent.SceneType sceneType) {
        if(sceneType.equals(SceneEvent.SceneType.LOGIN)){
//            Platform.runLater( () -> {
                showFXMLScene(getClass().getResource("/fxml/login.fxml"), loginController);
//            });

        }else if(sceneType.equals(SceneEvent.SceneType.CHOSE_CARDS)){
//            Platform.runLater( () -> {
                showFXMLScene(getClass().getResource("/fxml/chooseCard.fxml"), chooseCardController);
//                    });

        }else if(sceneType.equals(SceneEvent.SceneType.MAIN)){
//            Platform.runLater(()->{
                showMainScene(mainController);
//            });

        }else if(sceneType.equals(SceneEvent.SceneType.CONNECTION_CLOSED)){
//            showFXMLScene();
            showFXMLScene(getClass().getResource("/fxml/endGame.fxml"), loginController);
        }
    }



    public void showFXMLScene(URL resource, Object controller){
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(controller);
        fxmlLoader.setLocation(resource);
        Parent login = null;
        try {
            login = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(login);
//        stage.setResizable(false);
        stage.setResizable(true);
        stage.setScene(scene);

        stage.sizeToScene();

        stage.show();

    }

    public void showMainScene(MainController mainController){
//        for(int i = 0; i< GuiModel.getInstance().getNumPlayers(); i++)
//            GuiModel.getInstance().addCard(chosenCardsChallenger.get(i));
        stage.setResizable(true);
        stage.setScene(mainController.gameScene());
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.setMinHeight(540);
        stage.setMinWidth(720);
        stage.show();
    }
}
