package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.message.FailedSignUpMessage;
import it.polimi.ingsw.message.PingMessage;
import it.polimi.ingsw.message.ServerLobbyResponse;
import it.polimi.ingsw.model.event.*;

public class CliModelEventVisitor implements ModelEventVisitor, ControllerResponseVisitor, SetUpMessageVisitor{

    @Override
    public void visit(BuildWorkerModelEvent evt) {
        System.out.println("Build");
    }

    @Override
    public void visit(MoveWorkerModelEvent evt) {
        System.out.println("Move");
    }

    @Override
    public void visit(PlaceWorkerModelEvent evt) {
        System.out.println("Place");
    }

    @Override
    public void visit(ChosenCardsModelEvent evt) {
        System.out.println("ChosenCard");
    }

    @Override
    public void visit(FailModelEvent evt) {
        System.out.println("Fail");
    }

    @Override
    public void visit(FullInfoModelEvent evt) {
        System.out.println("FullInfo");
    }

    @Override
    public void visit(NewTurnModelEvent evt) {
        System.out.println("NewTurn");
        System.out.println(evt.getPlayer().getNickName());
    }

    @Override
    public void visit(PlayerDefeatModelEvent evt) {
        System.out.println("PlayerDefeat");
    }

    @Override
    public void visit(WinModelEvent evt) {
        System.out.println("Win");
    }

    @Override
    public void visit(SetCardModelEvent evt) {
        System.out.println("SetCard");
    }

    @Override
    public void visit(UndoModelEvent evt) {
        System.out.println("Undo");
    }

    @Override
    public void visit(FailedOperationControllerResponse r) {
        System.out.println("Failed operation");
    }

    @Override
    public void visit(FailedUndoControllerResponse r) {
        System.out.println("Failed undo");
    }

    @Override
    public void visit(IllegalCardNameControllerResponse r) {
        System.out.println("illegal card name");
    }

    @Override
    public void visit(IllegalCardNamesListControllerResponse r) {
        System.out.println("illegal list of card");
    }

    @Override
    public void visit(IllegalFirstPlayerControllerResponse r) {
        System.out.println("illegal first player");
    }

    @Override
    public void visit(IllegalTurnPhaseControllerResponse r) {
        System.out.println("illegal turn phase");
    }

    @Override
    public void visit(RequiredOperationControllerResponse r) {
        System.out.println("required operation");
    }

    @Override
    public void visit(NotCurrentPlayerControllerResponse r) {
        System.out.println("incorrectPlayer");
    }

    @Override
    public void visit(SuccessControllerResponse r) {
        System.out.println("correct operation");
    }

    @Override
    public void visit(TurnInfoControllerResponse r) {
        System.out.println("Turn info");
    }

    @Override
    public void visit(FailedSignUpMessage message) {
        if(message.getReason().equals(FailedSignUpMessage.Reason.INVALID_NICKNAME)) {
            System.out.println("Invalid nickName");
        }else if(message.getReason().equals(FailedSignUpMessage.Reason.NICKNAME_ALREADY_USED)) {
            System.out.println("NickName already used");
        }else {
            System.out.println("Incorrect num of players");
        }

    }

    @Override
    public void visit(PingMessage message) {

    }

    @Override
    public void visit(ServerLobbyResponse message) {
        if(message.getResponse().equals(ServerLobbyResponse.SingUpParameter.NICKNAME)) {
            System.out.println("Welcome, enter your nickname");
        }else if(message.getResponse().equals(ServerLobbyResponse.SingUpParameter.STARTGAME)) {
            System.out.println("Welcome, enter your nickname and num players");
        }else {
            System.out.println("Correct sign up, wait....");
        }
    }
}
