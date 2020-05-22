package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.SetUpMessageVisitor;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class GuiLoginSetUpMessageVisitor implements SetUpMessageVisitor {
    private final LoginController loginController;
    GuiLoginSetUpMessageVisitor(LoginController loginController){
        this.loginController = loginController;
    }
    @Override
    public void visit(SignUpFailedSetUpMessage message) {
        Platform.runLater( () -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("SignUpFailedSetUpMessage");
            alert.showAndWait();
        });
    }

    @Override
    public void visit(InitSetUpMessage evt) {
        Platform.runLater( () -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("InitSetUpMessage");
            alert.showAndWait();
        });
    }

    @Override
    public void visit(ConnectionMessage connectionMessage) {
        Platform.runLater( () -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("ConnectionMessage");
            alert.showAndWait();
        });
    }
}
