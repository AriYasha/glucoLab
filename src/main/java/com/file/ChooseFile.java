package com.file;

import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class ChooseFile {

    final static Logger logger = Logger.getLogger(ChooseFile.class);

    public static String chooseFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\GlucoLab"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            logger.debug(selectedFile.getName());
            return selectedFile.getName();
        } else {
            throw new IOException("File not selected");
        }

    }
}
