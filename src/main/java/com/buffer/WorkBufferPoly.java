package com.buffer;

import java.util.HashMap;
import java.util.Map;

public class WorkBufferPoly extends WorkBuffer {

    private static int currentPosition = 0;
    private static final Map<Integer, String> workBufferPoly = new HashMap<>(WORK_BUFFER_SIZE);

    public static void add(String filePath){
        if(currentPosition >= WORK_BUFFER_SIZE){
            currentPosition = 0;
        }
        workBufferPoly.put(currentPosition, filePath);
        currentPosition++;
    }

    public static Map<Integer, String> getWorkBufferMap() {
        return workBufferPoly;
    }

    public static void remove(Integer position){
        workBufferPoly.remove(position);
    }

    public static void remove(String string){
        workBufferPoly.remove(string);
    }

    public static void clear(){
        workBufferPoly.clear();
        currentPosition = 0;
    }

    public static String get(Integer position){
        return workBufferPoly.get(position);
    }

    public static int size() {
        return workBufferPoly.size();
    }
}
