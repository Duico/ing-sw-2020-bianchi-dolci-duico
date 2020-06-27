package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.model.event.*;

public interface ModelEventVisitor /*extends ClientEventEmitter*/ {
    /** Defines behavior in case of a BuildWorkerModelEvent
     * @param evt BuildWorkderModelEvent to be visited
     */
    void visit(BuildWorkerModelEvent evt);
    /** Defines behavior in case of a MoveWorkerModelEvent
     * @param evt MoveWorkerModelEvent to be visited
     */
    void visit(MoveWorkerModelEvent evt);
    /** Defines behavior in case of a PlaceWorkerModelEvent
     * @param evt PlaceWorkerModelEvent to be visited
     */
    void visit(PlaceWorkerModelEvent evt);
    /** Defines behavior in case of a ChosenCardsModelEvent
     * @param evt ChosenCardsModelEvent to be visited
     */
    void visit(ChosenCardsModelEvent evt); //send list of remaining Cards
    /** Defines behavior in case of a FailModelEvent
     * @param evt FailModelEvent to be visited
     */
    void visit(FailModelEvent evt);
    /** Defines behavior in case of a FullInfoModelEvent
     * @param evt FullInfoModelEvent to be visited
     */
    void visit(FullInfoModelEvent evt);
    /** Defines behavior in case of a NewTurnModelEvent
     * @param evt NewTurnModelEvent to be visited
     */
    void visit(NewTurnModelEvent evt); //ChoseCards, ChosePlayer
    /** Defines behavior in case of a PersistencyEvent
     * @param evt PersistencyEvent to be visited
     */
    void visit(PersistencyEvent evt);
    /** Defines behavior in case of a PlayerDefeatModelEvent
     * @param evt PlayerDefeatModelEvent to be visited
     */
    void visit(PlayerDefeatModelEvent evt);
    /** Defines behavior in case of a WinModelEvent
     * @param evt WinModelEvent to be visited
     */
    void visit(WinModelEvent evt);
    /** Defines behavior in case of a SetCardModelEvent
     * @param evt SetCardModelEvent to be visited
     */
    void visit(SetCardModelEvent evt);
    /** Defines behavior in case of a UndoModelEvent
     * @param evt UndoModelEvent to be visited
     */
    void visit(UndoModelEvent evt);
}

