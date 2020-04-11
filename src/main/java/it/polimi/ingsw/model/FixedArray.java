package it.polimi.ingsw.model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FixedArray<T> {
    private int size;
    private ArrayList<T> array;

    public FixedArray(int size){
        array = new ArrayList<T>();
    }
    public boolean add(T elem){
        for(int i=0; i<size; i++){
            if(array.get(i)==null){
                array.set(i, elem);
                return true;
            }
        }
        return false;
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

