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

    public static MeasurementSetup reading(String filename) {
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

    public static MeasureMode reading(MeasureMode mode) {
        if (MeasurementSetup.class == mode.getClass()) {
            mode = new MeasurementSetup();
        } else if (PolySetup.class == mode.getClass()) {

        }
        return mode;
    }

    private static MeasureMode readFromFile(String fileName) {
        return null;
    }
}