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
        guiModel.failedOperation();
        if(r.getOperation().equals(Operation.PLACE)){
            if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_FEASIBLE)){
                guiModel.setMessage("Your worker are already place");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.DESTINATION_NOT_EMPTY)){
                guiModel.setMessage("Position already occupied");
            }
        }else if(r.getOperation().equals(Operation.MOVE)){
            if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER)){
                guiModel.setMessage("Is not the current worker");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_ALLOWED)){
                guiModel.setMessage("You are not allowed to move");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.BLOCKED_BY_OPPONENT)){
                guiModel.setMessage("Ops, your opponent has blocked this move");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_FEASIBLE)){
                guiModel.setMessage("Not feasible move");
            }

        } else if(r.getOperation().equals(Operation.BUILD)){
            if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER)){
                guiModel.setMessage("Is not the current worker");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_ALLOWED)){
                guiModel.setMessage("You are not allowed to build");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_FEASIBLE)){
                guiModel.setMessage("Not feasible build");
            }

        }
    }

    @Override
    public void visit(FailedUndoControllerResponse r) {
        guiModel.setMessage("You're not allowed to undo anymore.");
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
            guiModel.setMessage("Your worker is required to move.");
        } else if(r.getRequiredOperation().equals(Operation.BUILD)){
            guiModel.setMessage("Your worker is required to build.");
        } else if(r.getRequiredOperation().equals(Operation.PLACE)) {
            guiModel.setMessage("Please, place all 2 workers");
        }
    }


    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {
        guiModel.setMessage("This is not your turn, wait...");
    }

    @Override
    public void visit(SuccessControllerResponse r) {

    }

    @Override
    public void visit(TurnInfoControllerResponse r) {
        guiModel.setButtonInfo(r.isAllowedToMove(), r.isAllowedToBuild(), r.isRequiredToMove(), r.isRequiredToBuild());

    }

    public void alert(String message){
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(message);
            alert.showAndWait();
        });
    }

}

