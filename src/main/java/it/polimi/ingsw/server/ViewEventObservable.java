package it.polimi.ingsw.server;

import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.List;


public class ViewEventObservable {
    private final List<ViewEventListener> observers = new ArrayList<>();


    /**
     * add an observer in the list of observers
     * @param observer the observer
     */
    public void addObserver(ViewEventListener observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * remove the observer from the list of observers
     * @param observer the observer
     */
    public void removeObserver(ViewEventListener observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }


    /**
     * notify the observers of an event
     * @param message the event
     */
    protected void eventNotify(ViewEvent message){
        synchronized (observers) {
            for(ViewEventListener observer : observers){
                observer.handleEvent(message);
            }
        }
    }

}
