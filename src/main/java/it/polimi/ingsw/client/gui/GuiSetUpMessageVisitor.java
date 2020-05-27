package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.SetUpMessageVisitor;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;
import javafx.application.Platform;

public class GuiSetUpMessageVisitor extends SetUpMessageVisitor {
    private final GuiModel guiModel;
    public GuiSetUpMessageVisitor(GuiModel guiModel) {
        super();
        this.guiModel = guiModel;
    }


    @Override
    public void visit(SignUpFailedSetUpMessage message) {
            if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME)) {
                //TODO.alert("Invalid nickName");
            }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS)){
                //TODO.alert("Incorrect num of players");
            }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.GAME_ALREADY_START)){
               //TODO.alert("Game already started, wait the end of the game...");
            }
    }

    @Override
    public void visit(InitSetUpMessage message) {
            if((message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
                //askNumPlayers = (message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)
                //TODO.setAskNumPlayers(askNumPlayers);
                Platform.runLater( ()-> {
                    //TODO.askSetUpInfo(askNumPlayers);
                });
            }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_LAST)) {
                boolean waitOtherPlayers=message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT);
                System.out.println("correct sign up");
                //TODO.correctSignUp(waitOtherPlayers);
            }
    }

    @Override
    public void visit(ConnectionMessage connectionMessage) {
            if(connectionMessage.getType().equals(ConnectionMessage.Type.DISCONNECTION)){
                //TODO.alert("End game, player disconnected");
                //CLOSE
            }else if(connectionMessage.getType().equals(ConnectionMessage.Type.DISCONNECTION_TOO_MANY_PLAYERS)){
                //TODO.alert("You have been kicked from the game, because there are too many players.");
                //CLOSE
            }
    }
}

