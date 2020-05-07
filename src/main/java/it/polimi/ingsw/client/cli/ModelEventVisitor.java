package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.event.*;

interface ModelEventVisitor {
    void visit(BuildWorkerModelEvent evt);
    void visit(MoveWorkerModelEvent evt);
    void visit(PlaceWorkerModelEvent evt);
    void visit(ChosenCardsModelEvent evt); //send list of remaining Cards
    void visit(FailModelEvent evt);
    void visit(FullInfoModelEvent evt);
    void visit(NewTurnModelEvent evt); //ChoseCards, ChosePlayer
    //void visit(PersistencyEvent evt);
    void visit(PlayerDefeatModelEvent evt);
    void visit(WinModelEvent evt);
    void visit(SetCardModelEvent evt);
    void visit(UndoModelEvent evt);
}
