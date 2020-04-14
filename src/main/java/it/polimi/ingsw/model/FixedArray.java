package it.polimi.ingsw.model;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FixedArray<T> implements Serializable {
    private int size;
    private ArrayList<T> array;

    public FixedArray(int size){
        this.size = size;
        array = new ArrayList<T>();
        for(int i=0; i<size;i++)
            array.add(null);
    }
    public int add(T elem){
        for(int i=0; i<size; i++){
            if(array.get(i)==null){
                array.set(i, elem);
                return i;
            }
        }
        return -1;
    }
    public T get(int index){
        if(index>=0 && index<size){
            return array.get(index);
        }
        return null;
    }
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

