package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.server.message.DisconnectionSetUpMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;

import java.io.PrintStream;
import java.util.Scanner;

public class CliSetUpMessageVisitor implements SetUpMessageVisitor {
    private PrintStream out = System.out;
    private CliInputHandler inputHandler;

    private CliController cliController;
    public CliSetUpMessageVisitor(CliController cliController){
        this.cliController = cliController;
    }

    @Override
    public void visit(SignUpFailedSetUpMessage message) {
        if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME)) {
            out.println(CliText.INVALID_NICKNAME.toString());


//            inputHandler.clearReadLines();
//            new Thread( () -> {
//                synchronized (inputHandler) {
//                    String line;
//                    while ((line = inputHandler.pollReadLines()) == null) {
//                        inputHandler.wait();
//                    }
//                    exec
//                }
//            });
            //askSetUpInfo(askNumPlayers);
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS)){
            out.println(CliText.INVALID_NUMPLAYERS.toString());
            //askSetUpInfo(askNumPlayers);
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.GAME_ALREADY_START)){
            out.println(CliText.GAME_ALREADY_STARTED.toString());
        }

    }

    @Override
    public void visit(DisconnectionSetUpMessage evt) {
        System.out.println(Color.YELLOW_BOLD.escape(System.lineSeparator()+"End game, player disconnected"));
    }

    @Override
    public void visit(InitSetUpMessage message) {
        boolean hasToWait;
        if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
            System.out.println(Color.YELLOW_BOLD.escape("Welcome, enter your nickname"));
//            askNumPlayers = false;
//            askSetUpInfo(false);
        }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)) {
            System.out.println(Color.YELLOW_BOLD.escape("Welcome, enter your nickname and num players"));
//            askNumPlayers = true;
//            askSetUpInfo(true);
        }else if( (hasToWait = message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT) ) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_LAST)) {
//            cliController.setMyPlayer(message.getPlayer());
//            printCorrectSignUp(hasToWait);
            //TODO REMOVE
            //cliController.isTurnOK = true;
        }

    }
}
