package com.controllers;

import com.entity.Data;
import com.file.ChooseFile;
import com.file.Read;
import com.graph.MultipleSameAxesLineChart;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    final static Logger logger = Logger.getLogger(GraphController.class);

    public LineChart glucoChart;
    public StackPane stackPane;
    public AnchorPane legendPane;
    public MenuBar menuBar;
    public NumberAxis xAxis;
    public NumberAxis yAxis;
    public Label legendLabel;

    private Data firstData;
    private String fileName;

    private boolean isOpen = false;

    MultipleSameAxesLineChart multipleAxesLineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBarSetup();
//        createMultiAxesLineChart();
//        multipleAxesLineChart.addSeries(prepareSeries(fileName, firstData), Color.color(Math.random(), 0, Math.random()), fileName);

    }

    public void setFirstData(Data firstData) {
        this.firstData = firstData;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void menuBarSetup() {
        menuBar.getMenus().clear();

        SeparatorMenuItem separator = new SeparatorMenuItem();

        Menu file = new Menu("Файл");

        MenuItem open = new MenuItem("Добавить");
        MenuItem close = new MenuItem("Выход");
        open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        close.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        open.setOnAction((event) -> {
            try {
                openPlotWindow();
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        });
        close.setOnAction((event) -> {
            isOpen = false;
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.close();
        });

        file.getItems().addAll(open, separator, close);

        menuBar.getMenus().addAll(file);
    }

    private void openPlotWindow() throws IOException {
        String fileName = ChooseFile.chooseFile();
        Data data = Read.reading(fileName);
      //  glucoChart.getData().add(prepareSeries(fileName, data));
        if (!isOpen) {
            createMultiAxesLineChart();
        }
        multipleAxesLineChart.addSeries(prepareSeries(fileName, data), Color.color(Math.random(), 0, Math.random()), fileName);
        legendLabel.setText(data.getMeasurementSetup().toString());
        //legendPane.getChildren().add(multipleAxesLineChart.getLegend());

        //glucoChart.getData().add(prepareSeries(fileName, data));
    }

    private void createMultiAxesLineChart() {
        isOpen = true;
        multipleAxesLineChart = new MultipleSameAxesLineChart(glucoChart, stackPane);
    }

    private XYChart.Series<Number, Number> prepareSeries(String name, Data data) {
        ArrayList<Number> xValues = (ArrayList<Number>) data.getCurrentXMeasurement();
        ArrayList<Number> yValues = (ArrayList<Number>) data.getCurrentYMeasurement();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (int i = 0; i < xValues.size(); i++) {
            series.getData().add(new XYChart.Data<>(xValues.get(i), yValues.get(i)));
        }
        return series;
    }
}
