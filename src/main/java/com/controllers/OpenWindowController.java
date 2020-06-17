package com.controllers;

import com.entity.MeasureMode;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import com.file.Read;
import com.file.Write;
import com.graph.VisualisationPlot;
import com.sample.CreateStage;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OpenWindowController implements Initializable {

    final static Logger logger = Logger.getLogger(OpenWindowController.class);

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
    public ImageView trashImage;
    public Button editButton;
    public ImageView editImage;
    public ComboBox underFiles;

    private boolean isNewWindow = true;


    private String path = "C:\\Glucolab";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceFile.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        viewFile.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        back.setBackground(null);
        ahead.setBackground(null);
        deleteFile.setBackground(null);
        editButton.setBackground(null);
        trashImage.setImage(new Image("images/trash.png"));
        editImage.setImage(new Image("images/edit.png"));
        chart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
//        chart.getXAxis().setTickLabelsVisible(false);
//        chart.getYAxis().setTickLabelsVisible(false);

        //getFileFromDirectory();
        getMainDirectories();
        getDirectoryFromMainDirectory();
        getFiles();

    }

    private void getMainDirectories(){
        File dir = new File(path);
        List<File> directories = getDirectory(dir);
        for (File file : directories) {
            if (file.isDirectory()) {
                files.getSelectionModel().selectFirst();
                files.getItems().add(file.getName());
            }
        }

    }


    private void getDirectoryFromMainDirectory(){
        File fromDirectory = new File(path+"\\"+files.getSelectionModel().getSelectedItem());
        List<File> underDirectory = getDirectory(fromDirectory);
        underFiles.getItems().clear();
        for (File f: underDirectory) {
            if (f.isDirectory()) {
                underFiles.getSelectionModel().selectFirst();
                underFiles.getItems().add(f.getName());
                System.out.println(f.getName());

            }
        }
    }

    public void setNewWindow(boolean newWindow) {
        isNewWindow = newWindow;
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

        getDirectoryFromMainDirectory();
        getFiles();
    }

    public void moveFiles(ActionEvent actionEvent) {
        ObservableList<String> selectedChoiceItems = choiceFile.getItems();
        ObservableList<String> selectedItems = viewFile.getSelectionModel().getSelectedItems();
        int count = 0;
        if(selectedChoiceItems.isEmpty()){
            choiceFile.getItems().addAll(viewFile.getSelectionModel().getSelectedItems());
        } else {
            for (int i = 0; i < selectedItems.size(); i++) {
                for (int j = 0; j < selectedChoiceItems.size(); j++) {
                    if (!selectedItems.get(i).equals(selectedChoiceItems.get(j))) {
                        count++;
                    }
                }
                     if (count == selectedChoiceItems.size()) {
                    if(selectedChoiceItems.size()>=10)
                     choiceFile.getItems().remove(0);
                    choiceFile.getItems().add(selectedItems.get(i));
                }

                     count = 0;
            }
        }
    }

    public void choiceFiles(MouseEvent mouseEvent) {
        ObservableList<String> selectedItems = viewFile.getSelectionModel().getSelectedItems();
        showDescription(selectedItems.get(0));


    }

    public void filesDescription(MouseEvent mouseEvent) {
        ObservableList<String> selectedItems = choiceFile.getSelectionModel().getSelectedItems();
        showDescription(selectedItems.get(0));
    }

    private void showDescription(String fileName) {
        if (fileName.contains(".gl")) {
            MeasurementSetup setup = (MeasurementSetup) Read.reading(
                    path + "\\" +
                            files.getSelectionModel().getSelectedItem() + "\\" +
                            underFiles.getSelectionModel().getSelectedItem() + "\\" +
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
        String newPath = path + "\\" + files.getValue()+"\\"+ underFiles.getValue();
        File newFile = new File(newPath);
        viewFile.getItems().clear();
        Map<Long,String> data = new HashMap<Long, String>();
        if (files.getValue() != null & underFiles.getValue()!=null) {
            ArrayList<File> path = new ArrayList<File>(Arrays.asList(newFile.listFiles()));
            for (File file : path) {
                System.out.println(file.lastModified());

                data.put(file.lastModified(),file.getName());
                }
        }
        TreeMap<Long, String> sorted = new TreeMap<>(data);


//        List<Long> sortData = new ArrayList<>(data.keySet());
//        Collections.sort(sortData);
//        for (Long l:sortData) {
//            System.out.println(l);
//            }
//


        for (Map.Entry<Long, String> pair : sorted.entrySet()) {
            viewFile.getItems().add(pair.getValue());
        }
//        ObservableList<String> allItems = viewFile.getItems();
//        for (String str: allItems) {

            

        }


    public void deleteFiles(ActionEvent actionEvent) {
        String newPath = path + "\\" + files.getValue();
        ObservableList<String> selectedItems = viewFile.getSelectionModel().getSelectedItems();
        for (String name : selectedItems) {
            File fileForDelete = new File(newPath + "\\" + name);
            fileForDelete.delete();
        }
        getFiles();
        textFromFile.setText("");
        chart.getData().clear();
    }

    public void openFiles(ActionEvent actionEvent) {
        ObservableList<String> selectedItems = choiceFile.getItems();
        logger.debug(selectedItems);
        if (!selectedItems.isEmpty()) {
            if (files.getValue().equals("Измерения")) {
                FXMLLoader fxmlLoader = openWindow("/fxml/graphMain.fxml", "Измерения глюкозы");
                GraphController graphController = fxmlLoader.getController();
                graphController.drawGraphics(selectedItems, Write.fileMeasurePath);

            } else if (files.getValue().equals("Полярограмма")) {
                FXMLLoader fxmlLoader = openWindow("/fxml/polyGraph.fxml", "Полярограмма");
                PolyGraphController polyGraphController = fxmlLoader.getController();
                polyGraphController.drawGraphics(selectedItems, Write.filePolyPath);
            }
            Stage stage = (Stage) openFile.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Огорчение!");
            alert.setHeaderText("Для открытия ничего не выбрано.");
            alert.setContentText("Поле \"Открыть записи\" не может быть пустым.");
            alert.showAndWait();
            logger.debug("items not selected");
        }
    }

    private FXMLLoader openWindow(String fxml, String title) {
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Parent root1 = fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage stage = new Stage();
            stage.setTitle(title);
            scene.getStylesheets().add("/styles/labStyle.css");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest((event) -> stage.close());
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return fxmlLoader;
    }

    public void getFilesFromDirectory(ActionEvent actionEvent) {
        getFiles();




    }

    public void editFileName(ActionEvent actionEvent) {
        CreateStage dialog = new CreateStage("hello");
    }
//    private void getFileFromDirectory(){
//        File fromDirectory = new File(path + "\\" + files.getSelectionModel().getSelectedItem());
//        System.out.println(fromDirectory.getName());
//        List<File> underDirectory = getDirectory(fromDirectory);
//        for (File f : underDirectory) {
//            if (f.isDirectory()) {
//                underFiles.getSelectionModel().selectFirst();
//                underFiles.getItems().add(f.getName());
//            }
//        }
//
//    }
}
