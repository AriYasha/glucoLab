package com.controllers;

import com.entity.MeasureMode;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import com.file.Read;
import com.graph.VisualisationPlot;
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
    public Button deleteFile;


    private String path = "C:\\Glucolab";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceFile.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        viewFile.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        back.setBackground(null);
        ahead.setBackground(null);
        deleteFile.setBackground(null);
        chart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        File dir = new File(path);
        ArrayList<File> forWork = new ArrayList<>();
        List<File> directories = getDirectory(dir);
        for (File file : directories) {
            if (file.isDirectory()) {
                files.getSelectionModel().selectFirst();
                files.getItems().add(file.getName());
            }
        }
        getFiles();
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

        getFiles();
    }

    public void moveFiles(ActionEvent actionEvent) {
        choiceFile.getItems().addAll(viewFile.getSelectionModel().getSelectedItems());

    }

    public void choiceFiles(MouseEvent mouseEvent) {
        ObservableList<String> selectedItems = viewFile.getSelectionModel().getSelectedItems();
        showDescription(selectedItems.get(0));


    }

    public void filesDescription(MouseEvent mouseEvent) {
        ObservableList<String> selectedItems = choiceFile.getSelectionModel().getSelectedItems();
        showDescription(selectedItems.get(0));
    }

    private void showDescription(String fileName){
        if (fileName.contains(".gl")) {
            MeasurementSetup setup = (MeasurementSetup) Read.reading(
                    path + "\\" +
                            files.getSelectionModel().getSelectedItem() + "\\" +
                            fileName);
            textFromFile.setText(setup.toString());
            chart.getData().clear();
            chart.getData().add(VisualisationPlot.prepareSeries(fileName, setup.getData()));
        } else if (fileName.contains(".pl")) {
            PolySetup setup = (PolySetup) Read.reading(
                    path + "\\" +
                            files.getSelectionModel().getSelectedItem() + "\\" +
                            fileName);
            textFromFile.setText(setup.toString());
            chart.getData().clear();
            chart.getData().add(VisualisationPlot.prepareSeries(fileName, setup.getData()));
        }
    }

    public void removeFiles(ActionEvent actionEvent) {
        ObservableList<String> selectedItems = choiceFile.getSelectionModel().getSelectedItems();
        choiceFile.getItems().removeAll(selectedItems);

    }

    private void getFiles() {
        String newPath = path + "\\" + files.getValue();
        File newFile = new File(newPath);
        viewFile.getItems().clear();
        if (files.getValue() != null) {
            ArrayList<File> path = new ArrayList<File>(Arrays.asList(newFile.listFiles()));
            for (File file : path) {
                viewFile.getItems().add(file.getName());
            }
        }
    }

    public void deleteFiles(ActionEvent actionEvent) {
        String newPath = path + "\\" + files.getValue();
        ObservableList<String> selectedItems = viewFile.getSelectionModel().getSelectedItems();
        for (String name : selectedItems) {
            File fileForDelete = new File(newPath + "\\" + name);
            fileForDelete.delete();
        }
        getFiles();
    }

    public void openFiles(ActionEvent actionEvent) {
    }
}
