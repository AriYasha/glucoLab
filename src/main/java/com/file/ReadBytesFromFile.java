package com.work.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ReadBytesFromFile {
    private String path = "one_8kHz_8bits.wav";

    public byte[] getBytesFromFile(){
        byte[] bytesFromFile = null;
        try {
            bytesFromFile = Files.readAllBytes(Paths.get(path));
            System.out.println(bytesFromFile.length);
            System.out.println(Arrays.toString(bytesFromFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesFromFile;
    }
}
