package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.client.SetUpMessageVisitor;
import it.polimi.ingsw.client.message.*;
import it.polimi.ingsw.server.message.*;

public class CliSetUpMessageVisitor extends ClientEventEmitter implements SetUpMessageVisitor {
    private final Cli cli;
    private final CliModel cliModel;
    private boolean askNumPlayers;
    private boolean askPersistency;

    public CliSetUpMessageVisitor(Cli cli, CliModel cliModel){
        this.cli = cli;
        this.cliModel = cliModel;
    }

    @Override
    public void visit(SignUpFailedSetUpMessage message) {
        if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME)) {
            cli.println(CliText.INVALID_NICKNAME.toString());
            cli.execInputRequest(askSetUpInfo(askNumPlayers, askPersistency));
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS)){
            cli.println(CliText.INVALID_NUMPLAYERS.toString());
            cli.execInputRequest(askSetUpInfo(askNumPlayers, askPersistency));
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.GAME_ALREADY_START)){
            cli.println(CliText.GAME_ALREADY_STARTED.toString());
        }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME_PERSISTENCY)){
            cli.println(CliText.INVALID_NICKNAME_PERSISTENCY.toString());
            cli.shutdown();
        }
    }

    @Override
    public void visit(InitSetUpMessage message) {
        if((askNumPlayers = message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
            askPersistency = askNumPlayers && message.isAskPersistency() ;
            cli.execInputRequest(askSetUpInfo(askNumPlayers, askPersistency));
        }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT)){
            cli.println(CliText.CORRECT_SIGNUP_WAIT);
        }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_STARTING)) {
//            boolean playerHasToWait = message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT);
            cliModel.setMyPlayer(message.getPlayer());
            cli.println(CliText.CORRECT_SIGNUP_LAST);
        }

    }

    @Override
    public void visit(ConnectionMessage connectionMessage) {
        if(connectionMessage.getType().equals(ConnectionMessage.Type.DISCONNECTION)){
            //cli.printClientConnectionEvent(connectionMessage);
            cli.print(System.lineSeparator() + Color.RED_UNDERLINED.escape("Game over, player disconnected..."));
            cli.shutdown();
        }else if(connectionMessage.getType().equals(ConnectionMessage.Type.DISCONNECTION_TOO_MANY_PLAYERS)){
            cli.print(System.lineSeparator() + Color.RED_UNDERLINED.escape("You have been kicked from the game, because there were too many players."));
            cli.shutdown();
        }
    }

    private CliRunnable askSetUpInfo(boolean askNumPlayers, boolean askPersistency) {
        return () -> {
            cli.clearReadLines();
            String playerName;
            Boolean persistency = false;
            if(askPersistency) {
                while ((persistency = promptPersistency()) == null) ;
            }
            cli.clearReadLines();
            while ((playerName = promptName()) == null) ;
            if (askNumPlayers) {
                Integer numPlayers = null;
                cli.clearReadLines();
                while ((numPlayers = promptNumPlayers()) == null);
                emitSignUp(new SignUpMessage(playerName, numPlayers, persistency));
            }else{
                emitSignUp(new SignUpMessage(playerName));
            }
//            }catch (InterruptedException e){
//                return;
//            }
        };
    }
    private String promptName() throws InterruptedException {
        cli.print(CliText.ASK_NAME.toPrompt());
        String line = cli.pollLine();
        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
            cli.println(CliText.BAD_NAME);
            return null;
        }
        return line;
    }
    private Integer promptNumPlayers() throws InterruptedException {
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
    private Boolean promptPersistency() throws InterruptedException {
        cli.print(CliText.ASK_PERSISTENCY.toPrompt());
        String line = cli.pollLine();
        if(line.toLowerCase().equals("yes")){
            return true;
        }
        if(line.toLowerCase().equals("no")){
            return false;
        }
        cli.println(CliText.BAD_YES_NO);
        return null;
    }

}