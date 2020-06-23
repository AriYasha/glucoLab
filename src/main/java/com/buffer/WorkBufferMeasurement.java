package com.buffer;

import java.util.HashMap;
import java.util.Map;

public class WorkBufferMeasurement extends WorkBuffer {

    private static int currentPosition = 0;
    private static final Map<Integer, String> workBufferMeasurement = new HashMap<>(WORK_BUFFER_SIZE);

    public static void add(String filePath){
        if(currentPosition >= WORK_BUFFER_SIZE){
            currentPosition = 0;
        }
        workBufferMeasurement.put(currentPosition, filePath);
        currentPosition++;
    }

    public static Map<Integer, String> getWorkBufferMap() {
        return workBufferMeasurement;
    }

    public static void remove(Integer position){
        workBufferMeasurement.remove(position);
    }

    public static void remove(String string){
        Integer position = null;
        for(Map.Entry<Integer, String> item : workBufferMeasurement.entrySet()){
            if (item.getValue().contains(string)){
                position = item.getKey();
            }
        }
        if(position != null) {
            workBufferMeasurement.remove(position);
        }
    }

    public static void clear(){
        workBufferMeasurement.clear();
        currentPosition = 0;
    }

    public static String get(Integer position){
        return workBufferMeasurement.get(position);
    }

    public static int size(){
        return workBufferMeasurement.size();
    }
}
