package it.polimi.ingsw.event;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

public abstract class EventEmitter {
    protected EventListenerList listenerList = new EventListenerList();

    synchronized public <T extends EventListener> void addEventListener(Class<T> listenerClass, T listener){
        listenerList.add(listenerClass, listener);
    }


    synchronized public <T extends EventListener> void removeEventListener(Class<T> listenerClass, T listener){
        listenerList.remove(listenerClass, listener);
    }

    synchronized public <T extends EventListener> void clearEventListeners(Class<T> listenerClass){
        Object[] listeners = listenerList.getListenerList();
        for(int i=0; i<listeners.length; i+=2){
            if(listeners[i] == listenerClass){
                listenerList.remove(listenerClass, (T) listeners[i+1]);
            }
        }
    }

    synchronized protected <T extends EventListener>  void executeEventListeners(Class<T> listenerClass, EventHandler<T> eventHandler){
        Object[] listeners = listenerList.getListenerList();
        /*
        i is increased by 2 at every loop because:
        even indices have Class<T>,
        odd indices have the effective EventListener
        */
        for(int i=0; i<listeners.length; i+=2){
            if(listeners[i] == listenerClass){
                eventHandler.handleEvent((T) listeners[i+1]);
            }
        }
    }
}
