package it.polimi.ingsw.client;
import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.controller.response.*;

public interface ControllerResponseVisitor /*extends ClientEventEmitter */{
    public abstract void visit(FailedOperationControllerResponse r);
    public abstract void visit(FailedUndoControllerResponse r);
    public abstract void visit(IllegalCardNameControllerResponse r);
    public abstract void visit(IllegalCardNamesListControllerResponse r);
    public abstract void visit(IllegalFirstPlayerControllerResponse r);
    public abstract void visit(IllegalTurnPhaseControllerResponse r);
    public abstract void visit(RequiredOperationControllerResponse r);
    public abstract void visit(NotCurrentPlayerControllerResponse r);
    public abstract void visit(SuccessControllerResponse r);
    public abstract void visit(TurnInfoControllerResponse r);
}
