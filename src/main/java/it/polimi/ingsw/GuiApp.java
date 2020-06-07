package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.event.MessageListener;
import it.polimi.ingsw.client.gui.*;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.view.ViewEventListener;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;

public class GuiApp extends Application {


    @Override
    public void start(Stage stage) {
        GuiModel guiModel = new GuiModel();
        LoginController loginController = new LoginController();
        loginController.setEventListener(guiModel);
        guiModel.setLoginController(loginController);

        ChooseCardController chooseCardController = new ChooseCardController();
        chooseCardController.setEventListener(guiModel);
        guiModel.setChooseCardController(chooseCardController);

        MainController mainController = new MainController();
        mainController.setEventListener(guiModel);
        guiModel.setMainController(mainController);

        SceneManager sceneManager = new SceneManager(stage);
        SceneEventEmitter sceneEventEmitter = sceneManager.startGame(guiModel.getLoginController(), guiModel.getChooseCardController(), guiModel.getMainController());
        GuiMessageVisitor guiMessageVisitor = new GuiMessageVisitor(guiModel, sceneEventEmitter);

//        ClientConnection clientConnection = new ClientConnection(argsIpAddress, argsPort, guiMessageVisitor);
        Map<String, String> params = getParameters().getNamed();
        String paramsIp = null;
        Integer paramsPort = null;
        if(params.containsKey("ip")){ //regexp
            paramsIp = params.get("ip");
        }else if(params.containsKey("addr")){
            paramsIp = params.get("addr");
        }
        if(paramsIp==null || !paramsIp.matches("^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$")){
            System.out.println("Missing or wrong IP address inserted. Reverting to default.");
            paramsIp = "127.0.0.1";
        }
        if(params.containsKey("port")){
            paramsPort = Integer.parseInt(params.get("port"));
        }
        if(paramsPort == null || paramsPort>65535 || paramsPort<1024){
            System.out.println("Missing or wrong port selected. Reverting to default.");
            paramsPort = 38612;
        }
        System.out.println("Connecting to "+paramsIp+":"+paramsPort);
        ClientConnection clientConnection = new ClientConnection(paramsIp, paramsPort, guiMessageVisitor);
        guiModel.addEventListener(ViewEventListener.class, clientConnection);
        guiModel.addEventListener(SignUpListener.class,clientConnection);
        stage.setOnCloseRequest((windowEvent) -> {
            clientConnection.closeConnection();
            System.out.println("Window closed... Closing connection.");
        });
        Thread connectionThread = new Thread(clientConnection::run);
        connectionThread.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
