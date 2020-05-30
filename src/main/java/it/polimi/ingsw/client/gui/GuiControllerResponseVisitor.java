package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.Operation;
import javafx.application.Platform;
import javafx.scene.control.Alert;

public class GuiControllerResponseVisitor implements ControllerResponseVisitor {
    private final GuiModel guiModel;
    public GuiControllerResponseVisitor(GuiModel guiModel) {
        this.guiModel = guiModel;
    }

    @Override
    public void visit(FailedOperationControllerResponse r) {
//        if(r.getOperation().equals(Operation.PLACE)){
            if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_FEASIBLE)){
//                System.out.println("PLACE ALREADY WORKER");
                if(r.getOperation().equals(Operation.PLACE))
                alert("YOUR WORKERS ARE ALREADY PLACED");
                else
                    alert("Move not feasible");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.DESTINATION_NOT_EMPTY)){
//                System.out.println("POSIZIONE OCCUPATA");
                alert("POSITION ALREADY OCCUPIED");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_ALLOWED)){
//                System.out.println("POSIZIONE OCCUPATA");
                alert("YOU ARE NOT ALLOWED TO DO THIS");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER)){
//                System.out.println("POSIZIONE OCCUPATA");
                alert("YOU CAN'T USE THIS WORKER");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.BLOCKED_BY_OPPONENT)){
//                System.out.println("POSIZIONE OCCUPATA");
                alert("YOU ARE BLOCKED");
            }
//        }
    }

    @Override
    public void visit(FailedUndoControllerResponse r) {

    }

    @Override
    public void visit(IllegalCardNameControllerResponse r) {

    }

    @Override
    public void visit(IllegalCardNamesListControllerResponse r) {

    }

    @Override
    public void visit(IllegalFirstPlayerControllerResponse r) {

    }

    @Override
    public void visit(IllegalTurnPhaseControllerResponse r) {

    }

    @Override
    public void visit(RequiredOperationControllerResponse r) {
        if(r.getRequiredOperation().equals(Operation.MOVE)){
            alert("Your worker hasn't moved yet");
        } else if(r.getRequiredOperation().equals(Operation.BUILD)){
            alert("You haven't built yet");
        } else if(r.getRequiredOperation().equals(Operation.PLACE)) {
            alert("You haven't placed both your workers");
        }
    }


    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {
        alert("This is not your turn");
    }

    @Override
    public void visit(SuccessControllerResponse r) {

    }

    @Override
    public void visit(TurnInfoControllerResponse r) {

    }

    public void alert(String message){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }

}

