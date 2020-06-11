package com.file;


import com.buffer.WorkBuffer;
import com.entity.MeasureMode;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Write {

    final static Logger logger = Logger.getLogger(Write.class);
    final public static String fileMeasurePath = "C:\\GlucoLab\\Измерения";
    final public static String filePolyPath = "C:\\GlucoLab\\Полярограмма";

    public static String generateFileName(MeasureMode mode) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        if (mode.getClass().getName().equals(MeasurementSetup.class.getName())) {
            return ((MeasurementSetup) mode).getData().getUserName() + " " + dateTimeFormatter.format(LocalDateTime.now()) + ".gl";
        } else if (mode.getClass().getName().equals(PolySetup.class.getName())) {
            return ((PolySetup) mode).getData().getUserName() + " " + dateTimeFormatter.format(LocalDateTime.now()) + ".pl";
        }
        return "";
    }

    public static String generatePackageName() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dateTimeFormatter.format(LocalDateTime.now());
    }


    public static void writing(MeasureMode mode, String fileName) {
        if (mode.getClass().getName().equals(MeasurementSetup.class.getName())) {
            createPackage(fileMeasurePath);
            createPackage(fileMeasurePath + "\\" + generatePackageName());
            writeObject(mode, fileMeasurePath + "\\" + generatePackageName() + "\\" + fileName);
            WorkBuffer.add(fileMeasurePath + "\\" + generatePackageName() + "\\" + fileName);
        } else if (mode.getClass().getName().equals(PolySetup.class.getName())) {
            createPackage(filePolyPath);
            createPackage(filePolyPath + "\\" + generatePackageName());
            writeObject(mode, filePolyPath + "\\" + generatePackageName() + "\\" + fileName);
        }
    }

    private static void writeObject(MeasureMode mode, String filePath) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(mode);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    private static void createPackage(String packageName){
        File directory = new File(packageName);
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (!created) {
                logger.warn("directory" + packageName +  "not created");
            }
        }
    }
}
