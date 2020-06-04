package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

import java.io.PipedOutputStream;

public abstract class WorkerViewEvent extends GameViewEvent {

    protected Position workerPosition;


    public WorkerViewEvent(Player player, Position workerPosition) {
        super(player);
        this.workerPosition = workerPosition;
    }

    public Position getWorkerPosition() {
        return workerPosition;
    }
    public abstract void accept(ViewEventVisitor visitor);

}

