package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

public class WorkerViewEvent extends ViewEvent {

    protected int workerId;


    public WorkerViewEvent(RemoteView view, int workerId) {
        super(view);
        this.workerId = workerId;
    }


    public int getWorkerId(){
        return this.workerId;
    }
}
