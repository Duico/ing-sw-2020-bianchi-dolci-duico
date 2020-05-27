package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.event.MessageListener;
import it.polimi.ingsw.client.gui.event.GuiEvent;
import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
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
    private final ClientConnection clientConnection;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private GuiModel guiModel;

    public SceneManager(Stage stage, ClientConnection clientConnection){
        this.stage = stage;
        this.clientConnection = clientConnection;
    }

    public void startGame(){
        guiModel = new GuiModel();
        LoginController loginController = new LoginController();
        loginController.addEventListener(GuiEventListener.class, guiModel);
        guiModel.setLoginController(loginController);

        ChooseCardController chooseCardController = new ChooseCardController();
        chooseCardController.addEventListener(GuiEventListener.class, guiModel);
        guiModel.setChooseCardController(chooseCardController);

        MainController mainController = new MainController();
        mainController.addEventListener(GuiEventListener.class, guiModel);
        guiModel.setMainController(mainController);


        GuiModelEventVisitor guiModelEventVisitor = new GuiModelEventVisitor(guiModel);
        guiModelEventVisitor.addSceneEventListener(this);

        GuiMessageVisitor guiMessageVisitor = new GuiMessageVisitor(guiModelEventVisitor, new GuiControllerResponseVisitor(guiModel), new GuiSetUpMessageVisitor(guiModel), new GuiClientConnectionEventVisitor(guiModel));
        setConnectionListener(guiMessageVisitor);
        guiModel.addEventListener(ViewEventListener.class, clientConnection);
        guiModel.addEventListener(SignUpListener.class,clientConnection);
        loadScene(SceneEvent.SceneType.LOGIN);
    }

    @Override
    public void handleEvent(SceneEvent evt) {
        if(evt.getSceneType().equals(guiModel.getSceneType())){
            return;
        }
            loadScene(evt.getSceneType());
    }


    private void setConnectionListener(GuiMessageVisitor guiMessageVisitor){
        clientConnection.addEventListener(MessageListener.class, guiMessageVisitor);
    }

    private void loadScene(SceneEvent.SceneType sceneType) {
        if(sceneType.equals(SceneEvent.SceneType.LOGIN)){
//            Platform.runLater( () -> {
                showFXMLScene(getClass().getResource("/fxml/login.fxml"), guiModel.getLoginController());
//            });

        }else if(sceneType.equals(SceneEvent.SceneType.CHOSE_CARDS)){
//            Platform.runLater( () -> {
                showFXMLScene(getClass().getResource("/fxml/chooseCard.fxml"), guiModel.getChooseCardController());
//                    });

        }else if(sceneType.equals(SceneEvent.SceneType.MAIN)){
//            Platform.runLater(()->{
                showMainScene(guiModel.getMainController());
//            });
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
        stage.setResizable(false);
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

        stage.show();
    }


}
