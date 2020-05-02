package it.polimi.ingsw.server;

import it.polimi.ingsw.view.ViewEventListener;

import java.util.ArrayList;
import java.util.List;

public class ObservableConnection {
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


    protected void notify(Object message){
        synchronized (observers) {
            for(ViewEventListener observer : observers){
                observer.handleEvent(message);
            }
        }
    }

}
