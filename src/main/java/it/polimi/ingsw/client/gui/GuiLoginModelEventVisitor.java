package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.event.*;

public class GuiLoginModelEventVisitor extends GuiModelEventVisitor {
    @Override
    public void visit(BuildWorkerModelEvent evt) {

    }

    @Override
    public void visit(MoveWorkerModelEvent evt) {

    }

    @Override
    public void visit(PlaceWorkerModelEvent evt) {

    }

    @Override
    public void visit(ChosenCardsModelEvent evt) {

    }

    @Override
    public void visit(FailModelEvent evt) {

    }

    @Override
    public void visit(FullInfoModelEvent evt) {

    }

    @Override
    public void visit(NewTurnModelEvent evt) {
        //load chooseCard scene

    }

    @Override
    public void visit(PlayerDefeatModelEvent evt) {

    }

    @Override
    public void visit(WinModelEvent evt) {

    }

    @Override
    public void visit(SetCardModelEvent evt) {

    }

    @Override
    public void visit(UndoModelEvent evt) {

    }
}