package com.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    public Button back;
    public Button ahead;



    String path = "C:\\Glucolab";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        List<File> filesList = (List<File>) FileUtils.listFiles(new File("C:\\Glucolab"), null, true);
//        for (File f:filesList) {
//         System.out.println(f.getName());
//}
        choiceFile.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        viewFile.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        File dir = new File("C:\\Glucolab");
        ArrayList<File> forWork = new ArrayList<>();
        List<File> directories = getDirectory(dir);
        for (File file:directories) {
            files.getItems().add(file.getName());
        }

    }


//    public ArrayList<File> getFiles(File inputVal,String directory, ArrayList<File> filess) {
//
//        if (inputVal.isDirectory()) {
//            ArrayList<File> path = new ArrayList<File>(Arrays.asList(inputVal.listFiles()));
//
//            for (int i = 0; i < path.size(); ++i) {
//                if (path.get(i).isDirectory() & path.get(i).equals(directory)) {
//                    addFiles(path.get(i),directory, filess);
//                }
//                if (path.get(i).isFile()) {
//                    filess.add(path.get(i));
//                }
//
//            }
//        }
//
//        if (inputVal.isFile()) {
//            filess.add(inputVal);
//        }
//
//        return filess;
//    }

    private List<File> getDirectory(File dir) {

        List<File> directory = (List<File>) Arrays.asList(dir.listFiles());

        return directory;
    }

    public void addFiles(ActionEvent actionEvent) {
        String newPath = path+"\\"+files.getValue();
        File newFile = new File(newPath);
        System.out.println(newPath);
        viewFile.getItems().clear();
        if(files.getValue()!=null) {
            ArrayList<File> path = new ArrayList<File>(Arrays.asList(newFile.listFiles()));
            for (File file:path) {
                viewFile.getItems().add(file.getName());
            }
        }
    }

    public void moveFiles(ActionEvent actionEvent) {
        choiceFile.getItems().addAll( viewFile.getSelectionModel().getSelectedItems());

    }

    public void choiceFiles(MouseEvent mouseEvent) {
        ObservableList<String> selectedItems =  viewFile.getSelectionModel().getSelectedItems();
    }

    public void removeFiles(ActionEvent actionEvent) {
        ObservableList<String> selectedItems = choiceFile.getSelectionModel().getSelectedItems();
        choiceFile.getItems().removeAll(selectedItems);

    }
}
