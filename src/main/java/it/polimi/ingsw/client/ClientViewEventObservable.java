package it.polimi.ingsw.client;

import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientViewEventObservable {
    private final List<ViewEventListener> listeners = new ArrayList<>();

    public void addViewEventListener(ViewEventListener listener){
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeViewEventListener(ViewEventListener listener){
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    protected void notify(ViewEvent message){
        synchronized (listeners) {
            for(ViewEventListener listener : listeners){
                listener.handleEvent(message);
            }
        }
    }
}
