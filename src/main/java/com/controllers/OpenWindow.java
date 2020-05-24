package com.controllers;

import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.*;

public class OpenWindow implements Initializable {
    public Pane workPane;
    public ComboBox files;
    public ListView viewFile;
    public ListView choiceFile;
    public Button openFile;
    public TextArea textFromFile;
    public LineChart chart;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<File> filesList = (List<File>) FileUtils.listFiles(new File("C:\\Glucolab"), null, false);
        for (File f:filesList) {
         System.out.println(f.getName());

        }
        File dir = new File("C:\\Glucolab");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                System.out.println(child.getAbsolutePath());
                files.getItems().add(child.getName());
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
        }
        ArrayList<File> files = new ArrayList<>();
          addfiles(dir,files);
    }


    private  void addfiles(File inputVal, ArrayList<File> filess) {
        if (inputVal.isDirectory()) {
            ArrayList<File> path = new ArrayList<File>(Arrays.asList(inputVal.listFiles()));

            for (int i = 0; i < path.size(); ++i) {
                if (path.get(i).isDirectory()) {
                    addfiles(path.get(i), filess);
                }
                if (path.get(i).isFile()) {
                    filess.add(path.get(i));
                }
            }

    /*  Optional : if you need to have the counts of all the folders and files you can create 2 global arrays
        and store the results of the above 2 if loops inside these arrays */
        }

        if (inputVal.isFile()) {
            filess.add(inputVal);
        }
        for (File f:filess) {
            System.out.println(f.getName());
        }

    }


}
