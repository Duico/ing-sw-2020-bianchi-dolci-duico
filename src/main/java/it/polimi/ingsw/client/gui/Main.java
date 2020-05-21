package it.polimi.ingsw.client.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Timer;

public class Main extends Application {

    LoginController login = new LoginController();

    public void timer(LoginController login) {
        java.util.Timer timer = new Timer();
        int delta = 5000;
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                login.setVisibleChoiceBox(false);
            }
        }, delta);

    }


    @Override
    public void start(Stage stage) throws Exception {
        LoginController login = new LoginController();
//        Scene scene = login.loginScene();
        stage.setScene(login.loginScene());
//        login.setVisibleChoiceBox(false);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
        timer(login);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
