package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.model.event.*;

public interface ModelEventVisitor /*extends ClientEventEmitter*/ {
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

/*
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
*/