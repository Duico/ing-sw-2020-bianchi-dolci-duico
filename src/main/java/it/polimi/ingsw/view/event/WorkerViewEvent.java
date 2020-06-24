package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.ViewEventVisitor;

/**
 * Represent generic event launched from client view when a worker tries to do any kind of operation
 */
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

