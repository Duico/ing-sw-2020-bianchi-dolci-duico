package it.polimi.ingsw.server;

import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.List;

public class ViewEventObservable {
    private final List<ViewEventListener> observers = new ArrayList<>();


    public void addObserver(ViewEventListener observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(ViewEventListener observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }


    protected void eventNotify(ViewEvent message){
        synchronized (observers) {
            for(ViewEventListener observer : observers){
                observer.handleEvent(message);
            }
        }
    }

}
