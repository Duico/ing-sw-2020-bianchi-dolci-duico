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
    public void visit(InitSetUpMessage message) {
        if((askNumPlayers = message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
            cli.execInputRequest(askSetUpInfo(askNumPlayers));

        }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_LAST)) {
            boolean playerHasToWait = message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT);
            cliModel.setMyPlayer(message.getPlayer());
            printCorrectSignUp(playerHasToWait);
        }

    }

    @Override
    public void visit(ConnectionMessage connectionMessage) {
        if(connectionMessage.getType().equals(ConnectionMessage.Type.DISCONNECTION) || connectionMessage.getType().equals(ConnectionMessage.Type.DISCONNECTION_TOO_MANY_PLAYERS)){
            cli.shutdown();
        }
    }

    private CliRunnable askSetUpInfo(boolean askNumPlayers) {
        return () -> {
            cli.clearReadLines();
            String playerName;
            while ((playerName = promptName()) == null);
            Integer numPlayers = null;
            if (askNumPlayers) {
                cli.clearReadLines();
                while ((numPlayers = promptNumPlayers()) == null);
            }
            emitSignUp(new SignUpMessage(playerName, numPlayers));
        };
    }
    private String promptName(){
        cli.print(CliText.ASK_NAME.toPrompt());
        String line = cli.pollLine();
        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
            cli.println(CliText.BAD_NAME);
            return null;
        }
        return line;
    }
    private Integer promptNumPlayers(){
        //check valid
        cli.print(CliText.ASK_NUMPLAYERS.toPrompt());
        String line = cli.pollLine();
        Integer num = 0;
        try {
            num = Integer.parseInt(line);
        }catch (NumberFormatException e){
            cli.println(CliText.BAD_NUMPLAYERS_STRING);
            return null;
        }
        if(num<2 || num >3){
            cli.println(CliText.BAD_NUMPLAYERS_INT);
            return null;
        }
        return num;
    }
    private void printCorrectSignUp(boolean hasToWait){
        if (hasToWait) {
            cli.println(CliText.CORRECT_SIGNUP_WAIT.toString());
        } else {
            cli.println(CliText.CORRECT_SIGNUP_LAST.toString());
        }
    }
}
