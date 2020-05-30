package it.polimi.ingsw.model;

import javax.swing.text.html.Option;
import java.io.Serializable;
import java.util.NoSuchElementException;

public class Optional<I> implements Serializable {
    boolean isPresent = false;
    I value = null;


    public static <T> Optional<T> empty(){
        return new Optional<T>();
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
