package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.model.event.*;

public abstract class ModelEventVisitor extends ClientEventEmitter {
    public abstract void visit(BuildWorkerModelEvent evt);
    public abstract void visit(MoveWorkerModelEvent evt);
    public abstract void visit(PlaceWorkerModelEvent evt);
    public abstract void visit(ChosenCardsModelEvent evt); //send list of remaining Cards
    public abstract void visit(FailModelEvent evt);
    public abstract void visit(FullInfoModelEvent evt);
    public abstract void visit(NewTurnModelEvent evt); //ChoseCards, ChosePlayer
    //void visit(PersistencyEvent evt);
    public abstract void visit(PlayerDefeatModelEvent evt);
    public abstract void visit(WinModelEvent evt);
    public abstract void visit(SetCardModelEvent evt);
    public abstract void visit(UndoModelEvent evt);
}
