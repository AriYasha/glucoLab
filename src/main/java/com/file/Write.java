package com.work.file;

import com.work.model.Data;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Write {

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

    public static void writing(Data data) {
        List<Data> records = Read.reading();
        boolean exist = false;
        if (records == null) {
            records = new ArrayList<>();
        }
        if (records != null && records.contains(data)) {
            exist = true;

        }
        if (!exist) {
            clear();
            records.add(data);
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("notes3.txt", true))) {
                objectOutputStream.writeObject(records);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
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
