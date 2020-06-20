
package it.polimi.ingsw.view.event;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.event.BuildWorkerModelEvent;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

import java.io.Serializable;


public class BuildViewEvent extends WorkerViewEvent implements Serializable {
    private Position destinationPosition;
    private boolean isDome;

    public BuildViewEvent(Player player, Position workerPosition, Position destinationPosition, boolean isDome){
        super(player, workerPosition);
        this.destinationPosition= destinationPosition;
        this.isDome=isDome;
    }
    public BuildViewEvent(Position workerPosition, Position destinationPosition, boolean isDome){
        this(null, workerPosition, destinationPosition, isDome);
    }

    public Position getDestinationPosition() {
        return destinationPosition;
    }

    public boolean isDome() {
        return isDome;
    }

    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }


}

