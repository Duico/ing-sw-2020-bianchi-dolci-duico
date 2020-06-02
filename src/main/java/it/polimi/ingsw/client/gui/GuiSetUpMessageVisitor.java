package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.SetUpMessageVisitor;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;

public class GuiSetUpMessageVisitor implements SetUpMessageVisitor {
    private final GuiModel guiModel;
    public GuiSetUpMessageVisitor(GuiModel guiModel) {
        super();
        this.guiModel = guiModel;
    }


    @Override
    public void visit(SignUpFailedSetUpMessage message) {
            if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME)) {
                alert("Invalid Nickname!");
            }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS)){
                alert("Incorrect number of players");
            }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.GAME_ALREADY_START)){
                alert("Game already started, wait the end of the game...");
            }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME_PERSISTENCY)){
                alert("Game loaded from disk has no player with this nickname.");
            }
    }

    @Override
    public void visit(InitSetUpMessage message) {
            if((message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
                boolean askNumPlayers = message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME);
                boolean isAskPersistency = message.isAskPersistency();
//                guiModel.setAskNumPlayers(askNumPlayers);
//                Platform.runLater( ()-> {
                    guiModel.askSetUpInfo(askNumPlayers, isAskPersistency);
//                });
            }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_STARTING)) {
                boolean starting=message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_STARTING);
                System.out.println("correct sign up");
                if(starting) {
                    guiModel.setMyPlayer(message.getPlayer());
                    //if persistency
                    //guiModel.startGame();
                }else {
                    guiModel.correctSignUp();
                }
            }
    }

    @Override
    public void visit(ConnectionMessage connectionMessage) {
            if(connectionMessage.getType().equals(ConnectionMessage.Type.DISCONNECTION)){
                alert("End game, player disconnected");
                //sceneEventEmitter closeWindow
            }else if(connectionMessage.getType().equals(ConnectionMessage.Type.DISCONNECTION_TOO_MANY_PLAYERS)){
                alert("You have been kicked from the game, because there are too many players.");
                //sceneEventEmitter closeWindow
            }
    }

    private void alert(String message){
        guiModel.alert(message);
    }
}

