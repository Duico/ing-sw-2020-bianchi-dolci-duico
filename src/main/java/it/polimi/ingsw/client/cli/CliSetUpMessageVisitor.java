package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.client.message.*;
import it.polimi.ingsw.server.message.*;

public class CliSetUpMessageVisitor extends ClientEventEmitter implements SetUpMessageVisitor {
    private final Cli cli;
    private CliModel cliModel;
    private boolean askNumPlayers;

    public CliSetUpMessageVisitor(Cli cli, CliModel cliModel){
        this.cli = cli;
        this.cliModel = cliModel;
    }

    @Override
    public void visit(SignUpFailedSetUpMessage message) {
        if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME)) {
            cli.println(CliText.INVALID_NICKNAME.toString());
            cli.execInputRequest(askSetUpInfo(askNumPlayers));
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS)){
            cli.println(CliText.INVALID_NUMPLAYERS.toString());
            cli.execInputRequest(askSetUpInfo(askNumPlayers));
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.GAME_ALREADY_START)){
            cli.println(CliText.GAME_ALREADY_STARTED.toString());
        }
    }

    @Override
    public void visit(DisconnectionSetUpMessage evt) {
        cli.println(Color.YELLOW_BOLD.escape(System.lineSeparator()+"End game, player disconnected"));
    }

    @Override
    public void visit(InitSetUpMessage message) {
        boolean hasToWait;

        if((askNumPlayers = message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
//            cli.println(Color.YELLOW_BOLD.escape("Welcome, enter your nickname and num players"));
            cli.execInputRequest(askSetUpInfo(askNumPlayers));

        }else if( (hasToWait = message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT) ) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_LAST)) {
            if (hasToWait) {
                cli.println(CliText.CORRECT_SIGNUP_WAIT.toString());
            } else {
                cli.println(CliText.CORRECT_SIGNUP_LAST.toString());
            }

//            cli.print(CliText.ASK_NAME.toPrompt());
//            cliController.setMyPlayer(message.getPlayer());
//            printCorrectSignUp(hasToWait);
            //TODO REMOVE
            //cliController.isTurnOK = true;
        }

    }

    private CliRunnable askSetUpInfo(boolean askNumPlayers) {
        return () -> {
            cli.clearReadLines();
            String line = null;
            String playerName;
            do {
//                synchronized (cli){
                cli.print(CliText.ASK_NAME.toPrompt());
                line = cli.pollLine();
//                }
            } while ((playerName = promptName(line)) == null);

//                    ask numPlayers (println)
            Integer numPlayers = null;
            if (askNumPlayers) {
                cli.clearReadLines();
                do {
                    cli.print(CliText.ASK_NUMPLAYERS.toPrompt());
                    line = cli.pollLine();
                } while ((numPlayers = promptNumPlayers(line)) == null);
            }
            emitSignUp(new SignUpMessage(playerName, numPlayers));
        };
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
}
