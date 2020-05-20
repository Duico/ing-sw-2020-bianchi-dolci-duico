package it.polimi.ingsw.client.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage stage) throws Exception {
        LoginController login = new LoginController();
        stage.setScene(login.loginScene());
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
