package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.model.event.*;

public class CliModelEventVisitor extends ClientEventEmitter implements ModelEventVisitor {
    private Cli cli;
    private CliModel cliModel;
    public CliModelEventVisitor(Cli cli, CliModel cliModel){
        this.cli = cli;
        this.cliModel = cliModel;
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
