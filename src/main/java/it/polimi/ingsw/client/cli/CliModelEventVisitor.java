package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.server.message.DisconnectionSetUpMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.model.event.*;

public class CliModelEventVisitor extends Cli implements ModelEventVisitor, ControllerResponseVisitor, SetUpMessageVisitor{

    @Override
    public void visit(BuildWorkerModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("Build"));
    }

    @Override
    public void visit(MoveWorkerModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("Move"));
    }

    @Override
    public void visit(PlaceWorkerModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("Place"));
    }

    @Override
    public void visit(ChosenCardsModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("ChosenCard"));
    }

    @Override
    public void visit(FailModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("Fail"));
    }

    @Override
    public void visit(FullInfoModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("FullInfo"));
    }

    @Override
    public void visit(NewTurnModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("NewTurn"));
        System.out.println(Color.YELLOW_BOLD.escape(evt.getPlayer().getNickName()));
    }

    @Override
    public void visit(PlayerDefeatModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("PlayerDefeat"));
    }

    @Override
    public void visit(WinModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("Win"));
    }

    @Override
    public void visit(SetCardModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("SetCard"));
    }

    @Override
    public void visit(UndoModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("Undo"));
    }

    @Override
    public void visit(FailedOperationControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("Failed operation"));
    }

    @Override
    public void visit(FailedUndoControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("Failed undo"));
    }

    @Override
    public void visit(IllegalCardNameControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("illegal card name"));
    }

    @Override
    public void visit(IllegalCardNamesListControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("illegal list of card"));
    }

    @Override
    public void visit(IllegalFirstPlayerControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("illegal first player"));
    }

    @Override
    public void visit(IllegalTurnPhaseControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("illegal turn phase"));
    }

    @Override
    public void visit(RequiredOperationControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("required operation"));
    }

    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("incorrectPlayer"));
    }

    @Override
    public void visit(SuccessControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("correct operation"));
    }

    @Override
    public void visit(TurnInfoControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("Turn info. Is Allowed to Move: "+r.isAllowedToMove() + "...etc"));
    }

    @Override
    public void visit(SignUpFailedSetUpMessage message) {
        if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME)) {
            out.println(CliText.INVALID_NICKNAME.toString());
            askSetUpInfo(askNumPlayers);
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS)){
            out.println(CliText.INVALID_NUMPLAYERS.toString());
            askSetUpInfo(askNumPlayers);
        }

    }

    @Override
    public void visit(DisconnectionSetUpMessage evt) {
        System.out.println(Color.YELLOW_BOLD.escape("End game, player disconnected"));
    }

    @Override
    public void visit(InitSetUpMessage message) {
        if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
//            System.out.println(Color.YELLOW_BOLD.escape("Welcome, enter your nickname"));
            askNumPlayers = false;
            askSetUpInfo(false);
        }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)) {
//            System.out.println(Color.YELLOW_BOLD.escape("Welcome, enter your nickname and num players"));
            askNumPlayers = true;
            askSetUpInfo(true);
        }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP)){
            out.println(CliText.CORRECT_SIGNUP.toString());
//            System.out.println(Color.YELLOW_BOLD.escape("Correct sign up, wait...."));
        }
    }

}
