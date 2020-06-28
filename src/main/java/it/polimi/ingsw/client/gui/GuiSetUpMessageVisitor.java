package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.SetUpMessageVisitor;
import it.polimi.ingsw.server.message.ConnectionMessage;
import it.polimi.ingsw.server.message.InitSetUpMessage;
import it.polimi.ingsw.server.message.SignUpFailedSetUpMessage;

/**
 * class which manages all set up messages received from server updating GUI current scene
 * we need only a single method to handle different types of set up messages
 */
public class GuiSetUpMessageVisitor implements SetUpMessageVisitor {
    private final SceneEventEmitter sceneEventEmitter;
    private final GuiModel guiModel;
    public GuiSetUpMessageVisitor(GuiModel guiModel, SceneEventEmitter sceneEventEmitter) {
        super();
        this.guiModel = guiModel;
        this.sceneEventEmitter=sceneEventEmitter;
    }


    /**
     * handles different reasons why sign up failed showing different messages on alert message box
     * @param message sign up message received
     */
    @Override
    public void visit(SignUpFailedSetUpMessage message) {
            if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME)) {
                alert("Invalid Nickname!");
            }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NUMPLAYERS)){
                alert("Incorrect number of players");
            }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.GAME_ALREADY_START)){
                alert("Game already started, wait the end of the game...");
            }else if(message.getReason().equals(SignUpFailedSetUpMessage.Reason.INVALID_NICKNAME_PERSISTENCY)){
                alert("Game loaded from disk has no player with nickname.");
            }
    }

    /**
     * updates login scene in a different way for first player connected to lobby and others
     * then updates scene if login was successfull
     * @param message set up message received
     */
    @Override
    public void visit(InitSetUpMessage message) {
            if((message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME)) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.NICKNAME)) {
                boolean askNumPlayers = message.getResponse().equals(InitSetUpMessage.SignUpParameter.STARTGAME);
                boolean isAskPersistency = message.isAskPersistency();
                guiModel.askSetUpInfo(askNumPlayers, isAskPersistency);
            }else if(message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_WAIT) || message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_STARTING)) {
                boolean starting=message.getResponse().equals(InitSetUpMessage.SignUpParameter.CORRECT_SIGNUP_STARTING);
//                System.out.println("correct sign up");
                if(starting) {
                    guiModel.setMyPlayer(message.getPlayer());
                }else {
                    guiModel.correctSignUp();
                }
            }
    }

    @Override
    public void visit(ConnectionMessage connectionMessage) {

    }

    private void alert(String message){
        guiModel.alert(message);
    }
}

