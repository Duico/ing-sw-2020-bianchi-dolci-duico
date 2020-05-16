package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.server.message.DisconnectionSetUpMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;

import java.io.PrintStream;
import java.util.Scanner;

public class CliSetUpMessageVisitor implements SetUpMessageVisitor {
    private final Cli cli;
    private CliController cliController;

    public CliSetUpMessageVisitor(Cli cli, CliController cliController){
        this.cli = cli;
        this.cliController = cliController;
    }

    @Override
    public void visit(SignUpFailedSetUpMessage message) {
        if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME)) {
            cli.println(CliText.INVALID_NICKNAME.toString());
            //askSetUpInfo(askNumPlayers);
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS)){
            cli.println(CliText.INVALID_NUMPLAYERS.toString());
            //askSetUpInfo(askNumPlayers);
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.GAME_ALREADY_START)){
            cli.println(CliText.GAME_ALREADY_STARTED.toString());
        }

    }

    @Override
    public void visit(DisconnectionSetUpMessage evt) {
        cli.println(Color.YELLOW_BOLD.escape(System.lineSeparator()+"End game, player disconnected"));
    }

    private String promptName(String line){
        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
            cli.println(CliText.BAD_NAME);
            return null;
        }
        return line;
    }
    private Integer promptNumPlayers(String line){
        //check valid
        Integer num = Integer.parseInt(line);

        if(num<2 || num >3){
            cli.println(CliText.BAD_NUMPLAYERS_INT);
            return null;
        }
        return num;
    }
    @Override
    public void visit(InitSetUpMessage message) {
        boolean hasToWait;
//        if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
//            cli.println(Color.YELLOW_BOLD.escape("Welcome, enter your nickname"));
//            askNumPlayers = false;
//            askSetUpInfo(false);
        if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
//            cli.println(Color.YELLOW_BOLD.escape("Welcome, enter your nickname and num players"));
            cli.execInputRequest((inputHandler)->{
                    inputHandler.clearReadLines();
                    String line = null;
                    String playerName;
                    cli.print(CliText.ASK_NAME.toPrompt());
                    line = cli.pollLine();
                    while((playerName = promptName(line)) == null) {
                        line = cli.pollLine();
                    }

//                    ask numPlayers (println)
                    inputHandler.clearReadLines();
                    Integer numPlayers;
                    cli.print(CliText.ASK_NUMPLAYERS.toPrompt());
                    line = cli.pollLine();
                    while((numPlayers = promptNumPlayers(line)) == null) {
                        line = cli.pollLine();
                    }
                    System.err.println(playerName);
                    System.err.println(numPlayers);
                    //cli.emitSignUp
                    //emitSignUp(new SignUpMessage(playerName, numPlayers));

//                String playerName;
//                Integer numPlayers = null;
//                while ((playerName = promptName(line)) == null);
////                if (askNumPlayers) {
////                    while ((numPlayers = promptNumPlayers()) == null) ;
////                }
//                emitSignUp(new SignUpMessage(playerName, numPlayers));
            });

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
