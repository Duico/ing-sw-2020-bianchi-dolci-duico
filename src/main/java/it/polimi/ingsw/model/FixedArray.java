package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Array with final size, which doesn't allow resizing
 * @param <T> Type of the objects inside the array
 */
public class FixedArray<T> implements Serializable {
    private final int size;
    private ArrayList<T> array;

    /**
     * Create a fixed size array of the provided size
     * All the elements are initially set to null
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
    public Optional<Integer> add(T elem){
        for(int i=0; i<size; i++){
            if(array.get(i)==null){
                array.set(i, elem);
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieve the element at the desired index
     * @param index Index of the element to get
     * @return Element at index
     */
    public T get(int index){
        if(index>=0 && index<size){
            return array.get(index);
        }else{
            throw new IndexOutOfBoundsException("FixedArray.get("+index+") out of bounds");
        }
    }

    /**
     * Sets to elem the element at index
     * @param index Index of the element that will be affected
     * @param elem  Element that will be set in place of the previous one
     * @return array with element set
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

    /**
     * Counts the null elements in the array
     * @return Count of the null elements
     */
    public int nullCount(){
        int count = 0;
       for (int i = size() - 1; i >= 0; i--) {
            if (get(i) == null) {
                count++;
            }
        }
       return count;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass() == getClass()){
            FixedArray<T> fa = (FixedArray<T>) obj;
            return Objects.equals(array, fa.array);

        }
        return super.equals(obj);
    }

    public FixedArray<?> clone(){
        FixedArray<?> fixedArray = null;
        try{
            fixedArray = (FixedArray<?>) super.clone();
        }catch(CloneNotSupportedException e){
            throw new RuntimeException("Clone not supported in FixedArray");
        }
        return fixedArray;
    }
}

