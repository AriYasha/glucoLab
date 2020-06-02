package com.file;


import com.entity.Data;
import com.entity.MeasureMode;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import org.apache.log4j.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Write {

    final static Logger logger = Logger.getLogger(Write.class);
    final public static String fileMeasurePath = "C:\\GlucoLab\\Измерения";
    final public static String filePolyPath = "C:\\GlucoLab\\Полярограмма";

    public static void writeNewData(MeasurementSetup measurementSetup) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        LocalDateTime nowDateTime = LocalDateTime.now();
        File directory = new File(fileMeasurePath);
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (!created) {
                logger.warn("directory not created");
            }
        }
        String fileName = measurementSetup.getData().getUserName() + " " + dateTimeFormatter.format(nowDateTime) + ".gl";
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileMeasurePath + "\\" + fileName))) {
            objectOutputStream.writeObject(measurementSetup);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    public static String generateFileName(MeasureMode mode) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        if (mode.getClass().getName().equals(MeasurementSetup.class.getName())) {
            return ((MeasurementSetup) mode).getData().getUserName() + " " + dateTimeFormatter.format(LocalDateTime.now()) + ".gl";
        } else if (mode.getClass().getName().equals(PolySetup.class.getName())) {
            return ((PolySetup) mode).getData().getUserName() + " " + dateTimeFormatter.format(LocalDateTime.now()) + ".pl";
        }
        return "";
    }

    public static void writePolyData(PolySetup polySetup) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        LocalDateTime nowDateTime = LocalDateTime.now();
        File directory = new File(filePolyPath);
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (!created) {
                logger.warn("directory not created");
            }
        }
        String fileName = polySetup.getData().getUserName() + " " + dateTimeFormatter.format(nowDateTime) + ".pl";
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePolyPath + "\\" + fileName))) {
            objectOutputStream.writeObject(polySetup);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex);
            ex.printStackTrace();
        }
    }


    public static void writing(MeasureMode mode, String fileName) {
        if (mode.getClass().getName().equals(MeasurementSetup.class.getName())) {
            File directory = new File(fileMeasurePath);
            if (!directory.exists()) {
                boolean created = directory.mkdir();
                if (!created) {
                    logger.warn("directory not created");
                }
            }
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileMeasurePath + "\\" + fileName))) {
                objectOutputStream.writeObject(mode);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                logger.error(ex);
                ex.printStackTrace();
            }
        } else if (mode.getClass().getName().equals(PolySetup.class.getName())) {
            File directory = new File(filePolyPath);
            if (!directory.exists()) {
                boolean created = directory.mkdir();
                if (!created) {
                    logger.warn("directory not created");
                }
            }
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePolyPath + "\\" + fileName))) {
                objectOutputStream.writeObject(mode);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                logger.error(ex);
                ex.printStackTrace();
            }
        }
    }
}
