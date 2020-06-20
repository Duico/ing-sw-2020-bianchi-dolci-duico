package it.polimi.ingsw.client;
import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.controller.response.*;

public interface ControllerResponseVisitor {
    void visit(FailedOperationControllerResponse r);
    void visit(FailedUndoControllerResponse r);
    void visit(IllegalCardNameControllerResponse r);
    void visit(IllegalCardNamesListControllerResponse r);
    void visit(IllegalFirstPlayerControllerResponse r);
    void visit(IllegalTurnPhaseControllerResponse r);
    void visit(RequiredOperationControllerResponse r);
    void visit(NotCurrentPlayerControllerResponse r);
    void visit(SuccessControllerResponse r);
    void visit(TurnInfoControllerResponse r);
}
