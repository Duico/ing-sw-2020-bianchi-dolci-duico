package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.event.MessageListener;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.event.Message;
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
    private final ClientConnection clientConnection;
    private FXMLLoader fxmlLoader = new FXMLLoader();

    public SceneManager(Stage stage, ClientConnection clientConnection){
        this.stage = stage;
        this.clientConnection = clientConnection;
    }
    @Override
    public void handleEvent(SceneEvent evt) {
        if(evt.getSceneType().equals(SceneEvent.SceneType.LOGIN)){
            LoginController loginController = new LoginController();
            showFXMLScene(getClass().getResource("/fxml/login.fxml"), loginController);
            GuiMessageVisitor guiMessageVisitor = new GuiLoginMessageVisitor(loginController);
            clientConnection.addEventListener(MessageListener.class, guiMessageVisitor);
            loginController.addEventListener(SignUpListener.class,clientConnection);

        }else if(evt.getSceneType().equals(SceneEvent.SceneType.CHOSE_CARDS)){
            ChooseCardController chooseCardController = new ChooseCardController();
            showFXMLScene(getClass().getResource("/fxml/chooseCard.fxml"), chooseCardController);
            GuiMessageVisitor guiMessageVisitor = new GuiChooseCardMessageVisitor(chooseCardController);
            clientConnection.addEventListener(MessageListener.class, guiMessageVisitor);
            chooseCardController.addEventListener(SignUpListener.class,clientConnection);

        }else if(evt.getSceneType().equals(SceneEvent.SceneType.MAIN)){
            showMainScene();
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

    public void showMainScene(){
        MainController mainController = new MainController();
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
