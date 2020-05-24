package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.event.MessageListener;
import it.polimi.ingsw.event.Message;

import java.io.Serializable;

public abstract class GameMessage extends Message implements Serializable {

}
