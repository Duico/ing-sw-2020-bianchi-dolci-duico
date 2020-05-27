package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.controller.response.*;

public class GuiControllerResponseVisitor extends ControllerResponseVisitor {
    private final GuiModel guiModel;
    public GuiControllerResponseVisitor(GuiModel guiModel) {
        this.guiModel = guiModel;
    }

    @Override
    public void visit(FailedOperationControllerResponse r) {

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
