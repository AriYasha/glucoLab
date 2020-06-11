package com.file;

import java.util.HashMap;
import java.util.Map;

public class WorkBuffer {

    private static final int WORK_BUFFER_SIZE = 10;
    private static int currentPosition = 0;

    private static final Map<Integer, String> workBufferMap = new HashMap<>(WORK_BUFFER_SIZE);

    public static void add(String filePath){
        if(currentPosition >= WORK_BUFFER_SIZE){
            currentPosition = 0;
        }
        workBufferMap.put(currentPosition, filePath);
        currentPosition++;
    }

    public static Map<Integer, String> getWorkBufferMap() {
        return workBufferMap;
    }

    public static void remove(Integer position){
        workBufferMap.remove(position);
//        currentPosition--;
    }

    public static void clear(){
        workBufferMap.clear();
        currentPosition = 0;
    }
}
