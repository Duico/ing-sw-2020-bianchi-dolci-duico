package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.event.MessageListener;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.event.Message;
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
    private SceneEvent.SceneType currentSceneType;
    private final ClientConnection clientConnection;
    private FXMLLoader fxmlLoader = new FXMLLoader();

    public SceneManager(Stage stage, ClientConnection clientConnection){
        this.stage = stage;
        this.clientConnection = clientConnection;
    }
    @Override
    public void handleEvent(SceneEvent evt) {
        if(evt.getSceneType().equals(this.currentSceneType)){
            return;
        }
            loadScene(evt.getSceneType());
    }

    private void loadScene(SceneEvent.SceneType sceneType) {
        if(sceneType.equals(SceneEvent.SceneType.LOGIN)){
            LoginController loginController = new LoginController();
            loginController.addEventListener(SignUpListener.class,clientConnection);
            loginController.addEventListener(ViewEventListener.class, clientConnection);
            GuiMessageVisitor guiMessageVisitor = new GuiLoginMessageVisitor(loginController);
            guiMessageVisitor.addSceneEventListener(this);
            clientConnection.addEventListener(MessageListener.class, guiMessageVisitor);
            Platform.runLater( () -> showFXMLScene(getClass().getResource("/fxml/login.fxml"), loginController));

        }else if(sceneType.equals(SceneEvent.SceneType.CHOSE_CARDS)){
            ChooseCardController chooseCardController = new ChooseCardController();
            chooseCardController.addEventListener(SignUpListener.class, clientConnection);
            chooseCardController.addEventListener(ViewEventListener.class, clientConnection);
            GuiMessageVisitor guiMessageVisitor = new GuiChooseCardMessageVisitor(chooseCardController);
            guiMessageVisitor.addSceneEventListener(this);
            clientConnection.addEventListener(MessageListener.class, guiMessageVisitor);
            Platform.runLater( () -> showFXMLScene(getClass().getResource("/fxml/chooseCard.fxml"), chooseCardController));

        }else if(sceneType.equals(SceneEvent.SceneType.MAIN)){
            MainController mainController = new MainController();
            //TODO
            showMainScene(mainController);
            GuiMessageVisitor guiMessageVisitor = new GuiMainMessageVisitor(mainController);
            guiMessageVisitor.addSceneEventListener(this);
            clientConnection.addEventListener(MessageListener.class, guiMessageVisitor);
            mainController.addEventListener(SignUpListener.class, clientConnection);
            mainController.addEventListener(ViewEventListener.class, clientConnection);
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
        stage.setScene(scene);

        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    public void showMainScene(MainController mainController){
//        for(int i = 0; i< GuiModel.getInstance().getNumPlayers(); i++)
//            GuiModel.getInstance().addCard(chosenCardsChallenger.get(i));
        stage.setScene(mainController.gameScene());
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.show();
    }


}
