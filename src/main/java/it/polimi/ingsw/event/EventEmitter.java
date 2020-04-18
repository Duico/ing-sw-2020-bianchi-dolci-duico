package it.polimi.ingsw.event;

import it.polimi.ingsw.event.Event;
import it.polimi.ingsw.event.EventHandler;
import it.polimi.ingsw.view.event.BuildViewEvent;
import it.polimi.ingsw.view.event.MoveViewEvent;
import it.polimi.ingsw.view.event.ViewEventListener;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

//TODO add comments
public class EventEmitter {
    protected EventListenerList listenerList = new EventListenerList();

    public void addEventListener(EventListener listener){
        listenerList.add(EventListener.class, listener);
    }


    public void removeEventListener(EventListener listener){
        listenerList.add(EventListener.class, listener);
    }

    protected void executeEventListeners(EventHandler eventHandler){
        Object[] listeners = listenerList.getListenerList();
        for(int i=0; i<listeners.length; i+=2){
            if(listeners[i] == EventListener.class){
                eventHandler.handleEvent((EventListener) listeners[i+1]);
            }
        }
    }

}
