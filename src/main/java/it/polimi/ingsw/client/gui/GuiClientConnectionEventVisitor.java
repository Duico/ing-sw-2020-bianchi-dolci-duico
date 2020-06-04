package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientConnectionEventVisitor;
import it.polimi.ingsw.client.event.ClientConnectionEvent;

public class GuiClientConnectionEventVisitor implements ClientConnectionEventVisitor {
    private final GuiModel guiModel;
    private final SceneEventEmitter sceneEventEmitter;
    protected GuiClientConnectionEventVisitor(GuiModel guiModel, SceneEventEmitter sceneEventEmitter) {
        this.guiModel = guiModel;
        this.sceneEventEmitter = sceneEventEmitter;
    }

    @Override
    public void visit(ClientConnectionEvent evt) {
        if(evt.getReason().equals(ClientConnectionEvent.Reason.ERROR_ON_THE_SOCKET)){
            guiModel.alert("Errore nella connessione con il server.");
        }else if(evt.getReason().equals(ClientConnectionEvent.Reason.DISCONNECTION_TOO_MANY_PLAYERS)) {
            guiModel.alert("You have been kicked from the game, because there are too many players.");
        }
        else{
            if(!guiModel.getEndGame()) {
                sceneEventEmitter.emitEvent(new SceneEvent(SceneEvent.SceneType.CONNECTION_CLOSED));
            }
        }
    }


}
