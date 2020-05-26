package com.file;



import com.entity.Data;
import com.entity.PolySetup;
import org.apache.log4j.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Write {

    final static Logger logger = Logger.getLogger(Write.class);
    final static String fileMeasurePath = "C:\\GlucoLab\\Измерения";
    final static String filePolyPath = "C:\\GlucoLab\\Полярограмма";

    public static void writing(String path, List<Integer> list) {
        try (FileWriter writer = new FileWriter(path, true)) {
            for (int i : list) {
                writer.write(Integer.toString(i));
                writer.append('\n');
            }
            writer.append('\n');
            writer.append('\n');
            writer.append("end");
            writer.append('\n');

            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void writing(String path, String string) {
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(string);
            writer.append('\n');

            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void writeNewData(Data data){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        LocalDateTime nowDateTime = LocalDateTime.now();
        File directory = new File(fileMeasurePath);
        if(!directory.exists()){
            boolean created = directory.mkdir();
            if(!created){
                logger.warn("directory not created");
            }
        }
        String fileName = data.getUserName() + " " + dateTimeFormatter.format(nowDateTime) + ".gl";
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileMeasurePath + "\\" + fileName))) {
            objectOutputStream.writeObject(data);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex);
            ex.printStackTrace();
        }
    }

    public static void writePolyData(PolySetup polySetup){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
        LocalDateTime nowDateTime = LocalDateTime.now();
        File directory = new File(filePolyPath);
        if(!directory.exists()){
            boolean created = directory.mkdir();
            if(!created){
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

    public static void clear() {
        try (FileWriter writer = new FileWriter("notes3.txt", false)) {
            writer.write("");
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
