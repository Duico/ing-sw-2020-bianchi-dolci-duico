package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.client.cli.CliText;
import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.Operation;

public class GuiControllerResponseVisitor implements ControllerResponseVisitor {
    private final GuiModel guiModel;
    public GuiControllerResponseVisitor(GuiModel guiModel) {
        this.guiModel = guiModel;
    }

    @Override
    public void visit(FailedOperationControllerResponse r) {
        if(r.getOperation().equals(Operation.PLACE)){
            if(r.getReason().equals(FailedOperationControllerResponse.Reason.NOT_FEASIBLE)){
                System.out.println("PLACE ALREADY WORKER");
            } else if(r.getReason().equals(FailedOperationControllerResponse.Reason.DESTINATION_NOT_EMPTY)){
                System.out.println("POSIZIONE OCCUPATA");
            }
        }
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

    }

    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {

    }

    @Override
    public void visit(SuccessControllerResponse r) {

    }

    @Override
    public void visit(TurnInfoControllerResponse r) {

    }
}
