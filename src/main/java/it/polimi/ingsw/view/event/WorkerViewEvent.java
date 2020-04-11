package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;

public class WorkerViewEvent extends ViewEvent {

    protected int workerId;


    public WorkerViewEvent(Player player, int workerId) {
        super(player);
        this.workerId = workerId;
    }


    public int getWorkerId(){
        return this.workerId;
    }
}
