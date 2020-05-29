package com.file;


import com.entity.Data;
import com.entity.MeasureMode;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Read {

    final static Logger logger = Logger.getLogger(Read.class);

    public static MeasurementSetup reading1(String filename) {
        String filePath = Write.fileMeasurePath;
        MeasurementSetup record = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(/*filePath +*/ filename))) {
            record = (MeasurementSetup) objectInputStream.readObject();
            logger.debug(record);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();

        }
        return record;
    }

    public static PolySetup readingPoly(String filename) {
        String filePath = Write.filePolyPath;
        PolySetup record = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(/*filePath +*/ filename))) {
            record = (PolySetup) objectInputStream.readObject();
            logger.debug(record);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();

        }
        return record;
    }

    public static MeasureMode reading(String filePath) {
        MeasureMode mode = null;
        if (filePath.contains(".gl")) {
            mode = readFromFile(filePath, new MeasurementSetup());
        } else if (filePath.contains(".pl")) {
            mode = readFromFile(filePath, new PolySetup());
        }
        return mode;
    }

    private static MeasureMode readFromFile(String filePath, MeasureMode mode) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            if(mode.getClass().getName().equals(MeasurementSetup.class.getName())){
                return (MeasurementSetup) objectInputStream.readObject();
            } else {
                return (PolySetup) objectInputStream.readObject();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();

        }
        return null;
    }
}