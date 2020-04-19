package it.polimi.ingsw.model;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Array with final size, which doesn't allow resizing
 * @param <T> Type of the objects inside the array
 */
public class FixedArray<T> implements Serializable {
    private final int size;
    private ArrayList<T> array;

    /**
     * Create a fixed size array of the provided size
     * @param size Final size of the array
     */
    public FixedArray(int size){
        this.size = size;
        array = new ArrayList<T>();
        for(int i=0; i<size;i++)
            array.add(null);
    }

    /**
     * Add an element at the first null index, if there is any
     * @param elem Element to add
     * @return index of the added element, -1 if not added
     */
    public int add(T elem){
        for(int i=0; i<size; i++){
            if(array.get(i)==null){
                array.set(i, elem);
                return i;
            }
        }
        return -1;
    }

    /**
     * Retrieve the element at the desired index
     * @param index Index of the element to get
     * @return Element at index
     */
    public T get(int index){
        if(index>=0 && index<size){
            return array.get(index);
        }
        return null;
    }

    /**
     * Sets to elem the element at index
     * @param index Index of the element that will be affected
     * @param elem  Element that will be set in place of the previous one
     * @return
     */
    public T set(int index, T elem){
        if(index>=0 && index<size){
            return array.set(index, elem);
        }
        return null;
    }

    public int size() {
        return size;
    }
}

