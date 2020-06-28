package it.polimi.ingsw.event;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

/**
 * abstract class used to emit events
 */
public abstract class EventEmitter {
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Add a listener to the list of listeners for a type of event
     * @param listenerClass the class of the listener, specifying the type of event the listener will listen to
     * @param listener the listener to be added in the list
     * @param <T> class of EventListener listenerClass must be a subclass of
     */
    synchronized public <T extends EventListener> void addEventListener(Class<T> listenerClass, T listener){
        listenerList.add(listenerClass, listener);
    }

    /**
     * Remove a listener from the list of listeners of a type of event
     * @param listenerClass the class of the listener
     * @param listener the listener to be removed from the list
     * @param <T> class of EventListener listenerClass must be a subclass of
     */
    synchronized public <T extends EventListener> void removeEventListener(Class<T> listenerClass, T listener){
        listenerList.remove(listenerClass, listener);
    }

    /**
     * Remove all listeners for a type of event
     * @param listenerClass the class of the listeners
     * @param <T> class of EventListener listenerClass must be a subclass of
     */
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
