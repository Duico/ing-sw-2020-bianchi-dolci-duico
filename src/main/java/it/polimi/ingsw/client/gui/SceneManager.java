package it.polimi.ingsw.client.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * class that handles scenes switch loading fxml's or other scenes
 */
public class SceneManager implements SceneEventListener {
    private final Stage stage;
    //private final ClientConnection clientConnection;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private SceneEvent.SceneType sceneType;
    private LoginController loginController;
    private ChooseCardController chooseCardController;
    private MainController mainController;

    public SceneManager(Stage stage){
        this.stage = stage;
    }

    /**
     * sets all game controller classes and launches login scene at the beginning
     * @param loginController login controller class
     * @param chooseCardController choose card controller class
     * @param mainController main game controller class
     * @return emitter of scene event's
     */
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


    /**
     * load game scene related to a scene type
     * @param sceneType scene type of scene to load
     */
    private void loadScene(SceneEvent.SceneType sceneType) {
        if(sceneType.equals(SceneEvent.SceneType.LOGIN)){
                showFXMLScene(getClass().getResource("/fxml/login.fxml"), loginController);

        }else if(sceneType.equals(SceneEvent.SceneType.CHOSE_CARDS)){
                showFXMLScene(getClass().getResource("/fxml/chooseCard.fxml"), chooseCardController);

        }else if(sceneType.equals(SceneEvent.SceneType.MAIN)){
                showMainScene(mainController);

        }else if(sceneType.equals(SceneEvent.SceneType.CONNECTION_CLOSED)){
            showFXMLScene(getClass().getResource("/fxml/endGame.fxml"), loginController);
        }
    }


    /**
     * loads fxml file from url and creates scene with it setting related controller class
     * @param resource url of fxml file
     * @param controller controller class of fxml file
     */
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
        stage.setResizable(false);
        stage.setScene(scene);

        stage.sizeToScene();

        stage.show();

    }

    /**
     * loads final game scene
     * @param mainController final game controller class
     */
    public void showMainScene(MainController mainController){
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
