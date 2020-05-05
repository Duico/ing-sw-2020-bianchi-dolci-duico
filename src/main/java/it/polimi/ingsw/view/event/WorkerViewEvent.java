package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;

import java.io.PipedOutputStream;

public class WorkerViewEvent extends GameViewEvent {

    protected Position workerPosition;


    public WorkerViewEvent(RemoteView view, Position workerPosition) {
        super(view);
        this.workerPosition = workerPosition;
    }

    public Position getWorkerPosition() {
        return workerPosition;
    }
    @Override
    public void accept(RemoteView visitor) {

    }

}

