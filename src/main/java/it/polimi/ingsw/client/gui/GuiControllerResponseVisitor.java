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
            if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_FEASIBLE)){
                if(r.getOperation().equals(Operation.PLACE))
                    guiModel.setMessage("YOUR WORKERS ARE ALREADY PLACED");
                else
                    guiModel.setMessage("MOVE NOT FEASIBLE");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.DESTINATION_NOT_EMPTY)){
                guiModel.setMessage("POSITION ALREADY OCCUPIED");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_ALLOWED)){
                guiModel.setMessage("YOU ARE NOT ALLOWED TO DO THIS");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_CURRENT_WORKER)){
                guiModel.setMessage("OCCUPIED POSITION");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.BLOCKED_BY_OPPONENT)){
                guiModel.setMessage("YOU ARE BLOCKED");
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
            guiModel.setMessage("YOUR WORKER HASN'T MOVED YET");
        } else if(r.getRequiredOperation().equals(Operation.BUILD)){
            guiModel.setMessage("YOUR WORKER HASN'T BUILT YET");
        } else if(r.getRequiredOperation().equals(Operation.PLACE)) {
            guiModel.setMessage("YOUR HAVEN'T PLACED BOTH YOUR WORKERS");
        }
    }


    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {
        guiModel.setMessage("THIS IS NOT YOUR TURN!");
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

