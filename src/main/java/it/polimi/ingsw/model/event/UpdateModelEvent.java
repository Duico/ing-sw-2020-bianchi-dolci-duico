package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.cli.ModelEventVisitor;
import it.polimi.ingsw.model.Player;

public abstract class UpdateModelEvent extends ModelEvent {
    public UpdateModelEvent(Player player) {
        super(player);
    }

    public abstract void accept(ModelEventVisitor visitor);

}
