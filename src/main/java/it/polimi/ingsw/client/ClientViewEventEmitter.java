package it.polimi.ingsw.client;

import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.event.EventEmitter;
import it.polimi.ingsw.view.ViewEventListener;
import it.polimi.ingsw.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientViewEventEmitter extends EventEmitter {
//    private final List<ViewEventListener> listeners = new ArrayList<>();

//    public void addViewEventListener(ViewEventListener listener){
//        synchronized (listeners) {
//            listeners.add(listener);
//        }
//    }
//
//    public void removeViewEventListener(ViewEventListener listener){
//        synchronized (listeners) {
//            listeners.remove(listener);
//        }
//    }
//
    protected void emitViewEvent(ViewEvent message){
        executeEventListeners(ViewEventListener.class, (listener)->{
            listener.handleEvent(message);
        });
    }
    protected void emitSignUp(SignUpMessage message){
        executeEventListeners(SignUpListener.class, (listener)->{
            listener.handleEvent(message);
        });
    }
}
