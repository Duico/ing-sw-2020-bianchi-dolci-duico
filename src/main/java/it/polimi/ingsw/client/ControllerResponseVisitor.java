package it.polimi.ingsw.client;
import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.controller.response.*;

/**
 * class that allows to do different operations using a single method which can handle all
 * different types of controller response events
 */
public interface ControllerResponseVisitor {
    /** Defines behavior in case of a FailedOperationControllerResponse
     * @param r FailedOperationControllerResponse to be visited
     */
    void visit(FailedOperationControllerResponse r);

    /** Defines behavior in case of a FailedUndoControllerResponse
     * @param r FailedUndoControllerResponse to be visited
     */
    void visit(FailedUndoControllerResponse r);

    /** Defines behavior in case of a IllegalCardNameControllerResponse
     * @param r IllegalCardNameControllerResponse to be visited
     */
    void visit(IllegalCardNameControllerResponse r);

    /** Defines behavior in case of a IllegalCardNamesListControllerResponse
     * @param r IllegalCardNamesListControllerResponse to be visited
     */
    void visit(IllegalCardNamesListControllerResponse r);

    /** Defines behavior in case of a IllegalFirstPlayerControllerResponse
     * @param r IllegalFirstPlayerControllerResponse to be visited
     */
    void visit(IllegalFirstPlayerControllerResponse r);

    /** Defines behavior in case of a IllegalTurnPhaseControllerResponse
     * @param r IllegalTurnPhaseControllerResponse to be visited
     */
    void visit(IllegalTurnPhaseControllerResponse r);

    /** Defines behavior in case of a RequiredOperationControllerResponse
     * @param r RequiredOperationControllerResponse to be visited
     */
    void visit(RequiredOperationControllerResponse r);

    /** Defines behavior in case of a NotCurrentPlayerControllerResponse
     * @param r NotCurrentPlayerControllerResponse to be visited
     */
    void visit(NotCurrentPlayerControllerResponse r);

    /** Defines behavior in case of a SuccessControllerResponse
     * @param r SuccessControllerResponse to be visited
     */
    void visit(SuccessControllerResponse r);

    /** Defines behavior in case of a TurnInfoControllerResponse
     * @param r TurnInfoControllerResponse to be visited
     */
    void visit(TurnInfoControllerResponse r);
}
