package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 *Clone of the Optional class which also implements Serializable.
 * @param <I> type of the value enclosed in the optional
 */
public class Optional<I> implements Serializable {
    private boolean isPresent = false;
    private I value = null;

    /**
     * Create an empty Optional of the required type.
     * The returned Optional represents absence of a value.
     * @param <T> the type of the new Optional
     * @return the empty Optional
     */
    public static <T> Optional<T> empty(){
        return new Optional<>();
    }

    /**
     * Create a new Optional containing the desired value/
     * @param val the value to be wrapped in the Optional
     * @param <T> the type of the new Optional
     * @return the new Optional containing val
     * @throws NullPointerException if val is null
     */
    public static <T> Optional<T> of(T val) throws NullPointerException{
        if(val==null) throw new NullPointerException();
        Optional<T> optional = new Optional<>();
        optional.value = val;
        optional.isPresent = true;
        return optional;
    }

    /**
     *Used to check that a value is present before calling Optional.get()
     * @return true if the Optional contains a value, i.e. it is not empty
     */
    public boolean isPresent() {
        return isPresent;
    }

    /**
     * Get the value contained in the Optional, if present.
     * @return the value
     * @throws NoSuchElementException if the Optional is empty
     */
    public I get() throws NoSuchElementException{
        if(isPresent){
            return value;
        }else{
            throw new NoSuchElementException();
        }
    }
}
