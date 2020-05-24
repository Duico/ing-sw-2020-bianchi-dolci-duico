package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.event.*;

public class GuiModelEventVisitor implements ModelEventVisitor {

    //gamecontroller
    //chooseCardController

    public GuiModelEventVisitor(){
    }


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
        if(evt.getTurnPhase().equals(TurnPhase.CHOSE_CARDS)){
            //load scene chooseCard fxml
        }
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
