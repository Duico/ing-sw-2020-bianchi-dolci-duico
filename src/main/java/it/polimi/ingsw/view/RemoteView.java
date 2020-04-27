package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.response.ControllerResponse;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.event.BoardListener;
import it.polimi.ingsw.model.event.ModelEvent;
//import it.polimi.ingsw.server.ClientConnection;

public class RemoteView extends View {


    //private final ClientConnection connection;

    public RemoteView(/*ClientConnection connection,*/ Player player){
        super(player);
        //this.connection = connection;
    }

    public void eventResponse(ControllerResponse response){
        //send to Client View
    }


}
