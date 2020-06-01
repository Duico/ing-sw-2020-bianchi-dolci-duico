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
    private GuiModel guiModel;
    private SceneEvent.SceneType sceneType;

    public SceneManager(Stage stage/*, ClientConnection clientConnection*/){
        this.stage = stage;
        //this.clientConnection = clientConnection;
    }

    public void startGame(){
        guiModel = new GuiModel();
        LoginController loginController = new LoginController();
        loginController.setEventListener(guiModel);
        guiModel.setLoginController(loginController);

        ChooseCardController chooseCardController = new ChooseCardController();
        chooseCardController.setEventListener(guiModel);
        guiModel.setChooseCardController(chooseCardController);

        MainController mainController = new MainController();
        mainController.setEventListener(guiModel);
        guiModel.setMainController(mainController);


        SceneEventEmitter sceneEventEmitter = new SceneEventEmitter();
        sceneEventEmitter.addEventListener(SceneEventListener.class, this);
        //GuiModelEventVisitor guiModelEventVisitor = new GuiModelEventVisitor(guiModel, sceneEventEmitter);
        //GuiClientConnectionEventVisitor guiClientConnectionEventVisitor = new GuiClientConnectionEventVisitor(guiModel, sceneEventEmitter);
        //GuiMessageVisitor guiMessageVisitor = new GuiMessageVisitor(guiModelEventVisitor, new GuiControllerResponseVisitor(guiModel), new GuiSetUpMessageVisitor(guiModel), guiClientConnectionEventVisitor);
        //setConnectionListener(guiMessageVisitor);
        MessageVisitor guiMessageVisitor = new GuiMessageVisitor(guiModel, sceneEventEmitter);
//        ClientConnection clientConnection = new ClientConnection("3.137.63.131", 11347, guiMessageVisitor);
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 38612, guiMessageVisitor);
        guiModel.addEventListener(ViewEventListener.class, clientConnection);
        guiModel.addEventListener(SignUpListener.class,clientConnection);
        this.sceneType= SceneEvent.SceneType.LOGIN;
        loadScene(SceneEvent.SceneType.LOGIN);
        Thread connectionThread = new Thread(clientConnection::run);
        connectionThread.start();

        stage.setOnCloseRequest((windowEvent) -> {
            //disconnect
            //TODO handle interruptedException
            clientConnection.closeConnection();
            System.out.println("Window closed... Closing connection.");
        });
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
        }else if(sceneType.equals(SceneEvent.SceneType.CONNECTION_CLOSED)){
//            showFXMLScene();
            System.exit(1);
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
