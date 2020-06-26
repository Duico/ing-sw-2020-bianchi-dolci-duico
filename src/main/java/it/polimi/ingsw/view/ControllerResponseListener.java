package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.response.ControllerResponse;

import java.util.EventListener;

/**
 * listener interface for controller response messages
 */
public interface ControllerResponseListener extends EventListener {
    void eventResponse(ControllerResponse evt);
}
