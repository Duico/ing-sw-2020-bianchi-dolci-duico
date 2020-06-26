package it.polimi.ingsw.controller.response;
import it.polimi.ingsw.client.ControllerResponseVisitor;
import it.polimi.ingsw.view.event.ViewEvent;

/**
 * special response sent from controller to view which contains informations about current turn state
 * used in GUIapp to update game buttons
 */
public class TurnInfoControllerResponse extends ControllerResponse {
    private boolean isRequiredToMove;
    private boolean isRequiredToBuild;
    private boolean isAllowedToMove;
    private boolean isAllowedToBuild;
    private boolean isUndoAvailable;

    public TurnInfoControllerResponse(ViewEvent event, boolean isRequiredToMove, boolean isRequiredToBuild, boolean isAllowedToMove, boolean isAllowedToBuild, boolean isUndoAvailable) {
        super(event);
        this.isRequiredToMove=isRequiredToMove;
        this.isRequiredToBuild=isRequiredToBuild;
        this.isAllowedToMove=isAllowedToMove;
        this.isAllowedToBuild=isAllowedToBuild;
        this.isUndoAvailable=isUndoAvailable;
    }

    public boolean isRequiredToMove() {
        return isRequiredToMove;
    }

    public boolean isRequiredToBuild() {
        return isRequiredToBuild;
    }

    public boolean isAllowedToMove() {
        return isAllowedToMove;
    }

    public boolean isAllowedToBuild() {
        return isAllowedToBuild;
    }

    public boolean isUndoAvailable() {
        return isUndoAvailable;
    }

    public void accept(ControllerResponseVisitor visitor){
        visitor.visit(this);
    }
}
