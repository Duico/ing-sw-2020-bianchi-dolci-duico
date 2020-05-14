package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.controller.response.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.server.message.DisconnectionSetUpMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.model.event.*;

import java.util.List;

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
    synchronized public void visit(ChosenCardsModelEvent evt) {
        //System.out.println(Color.YELLOW_BOLD.escape("Chosen Cards"));
        List<String> cardDeck = evt.getCardDeck();
        List<String> cards = evt.getChosenCards();
        Player player = evt.getPlayer();
//        System.err.println("ChosenCards "+evt.toString());

//        System.err.println(evt);
//        System.err.println("Event's player:"+ evt.getPlayer().getUuid());
//        System.err.println("My player"+cliController.getMyPlayer().getUuid());
        cliController.setCards(cards);
        cliController.setCardDeck(cardDeck);
//        cliController.choseCardPlayer = evt.getPlayer();
        //TODO REMOVE
        synchronized (cliController) {
            cliController.isTurnOK = true;
            cliController.notifyAll();
        }
//        if(!player.equalsUuid(cliController.getMyPlayer())){
//            //not my turn
//            return;
//        }
//        if(cards == null){ //pick cards
//            if(cardDeck != null){
//                askChallCards(cardDeck);
//            }else{
//                //error
//            }
//        }else if(cards.size() == 1){
//            //server will automatically pick last card for you
//            //ASK firstPlayer
////            synchronized(askFirstPlayerLock) {
////                    while (cliController.getPlayerCard(cliController.getMyPlayer()) == null) {
////                        askFirstPlayerLock.wait();
////                    }
//                    askFirstPlayer();
//
//
//        }else if(cards.size() > 1){
//            askCard(cards);
//        }

    }

    @Override
    public void visit(FailModelEvent evt) {
        System.out.println(Color.YELLOW_BOLD.escape("Fail"));
    }

    @Override
    public void visit(FullInfoModelEvent evt) {
        cliController.board = evt.getBoard();
        cliController.players = evt.getPlayers();
    }

    @Override
    public void visit(NewTurnModelEvent evt) {
        //synchronized (out) {
            Player player = evt.getPlayer();
            TurnPhase turnPhase = evt.getTurnPhase();
            cliController.setTurnPhase(turnPhase);
            cliController.setPlayersIfNotSet(evt.getPlayers());
            cliController.setCurrentPlayer(player);
            boolean myTurn = player.equalsUuid(cliController.getMyPlayer());
//            System.err.println("NewTurn "+evt.toString());



//            if (player.equalsUuid(cliController.getMyPlayer())) {
//                myTurn = true;
//                switch (turnPhase) {
//                    case CHOSE_CARDS:
//                        break;
//                    case PLACE_WORKERS:
//                    case NORMAL:
//                        //do nothing (cliController.setTurnPhase is enough)
//                        //wait for command to be input
//                        break;
//                }
//            } else {
//                myTurn = false;
//            }
            //hasPrintedTurnMessage = true;
            //out.notifyAll();
            nextOperation(true, myTurn);
//            //TODO REMOVE
            if(evt.getTurnPhase() == TurnPhase.CHOSE_CARDS){
                cliController.isTurnOK = false;
            }
//        //}
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
        cliController.setPlayerCard(evt.getPlayer(), evt.getCardName());

//        System.err.println("SetCard "+evt.toString());

        CliText cliText;
        if(cliController.getMyPlayer().equalsUuid(evt.getPlayer())){
            cliText = CliText.SET_CARD_OWN;
        }else{
            cliText = CliText.SET_CARD_OTHER;
        }

        //TODO generalize
        synchronized (out) {
            while (waitingForInput == true) {
                try {
                    out.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            infoOut.println();
            infoOut.print(cliText.toString(evt.getCardName(), evt.getPlayer().getNickName()));
        }
//        askFirstPlayerLock.notifyAll();

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
        System.err.println(r.getEvent().getClass());
        System.out.println(Color.YELLOW_BOLD.escape("incorrectPlayer"));
    }

    @Override
    public void visit(SuccessControllerResponse r) {
        System.out.println(Color.YELLOW_BOLD.escape("\ncorrect operation"));
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
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.GAME_ALREADY_START)){
            out.println(CliText.GAME_ALREADY_START.toString());
        }

    }

    @Override
    public void visit(DisconnectionSetUpMessage evt) {
        System.out.println(Color.YELLOW_BOLD.escape("End game, player disconnected"));
    }

    @Override
    public void visit(InitSetUpMessage message) {
        boolean hasToWait;
        if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
//            System.out.println(Color.YELLOW_BOLD.escape("Welcome, enter your nickname"));
            askNumPlayers = false;
            askSetUpInfo(false);
        }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)) {
//            System.out.println(Color.YELLOW_BOLD.escape("Welcome, enter your nickname and num players"));
            askNumPlayers = true;
            askSetUpInfo(true);
        }else if( (hasToWait = message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT) ) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_LAST)) {
            cliController.setMyPlayer(message.getPlayer());
            printCorrectSignUp(hasToWait);
            //TODO REMOVE
            //cliController.choseCardTurnOK = true;
        }

    }

}
