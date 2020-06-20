package it.polimi.ingsw.model;

import javax.swing.text.html.Option;
import java.io.Serializable;
import java.util.NoSuchElementException;

public class Optional<I> implements Serializable {
    private boolean isPresent = false;
    private I value = null;


    public static <T> Optional<T> empty(){
        return new Optional<>();
    }
    public static <T> Optional<T> of(T val) throws NullPointerException{
        if(val==null) throw new NullPointerException();
        Optional<T> optional = new Optional<>();
        optional.value = val;
        optional.isPresent = true;
        return optional;
    }
    public boolean isPresent() {
        return isPresent;
    }
    public I get() throws NoSuchElementException{
        if(isPresent){
            return value;
        }else{
            throw new NoSuchElementException();
        }
    }
}
