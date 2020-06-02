package com.controllers;

import com.entity.MeasureMode;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import com.file.Read;
import com.file.Write;
import com.graph.MultipleSameAxesLineChart;
import com.graph.VisualisationPlot;
import javafx.collections.ObservableList;
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
import java.util.HashMap;
import java.util.Map;

public abstract class DrawMeasure {

    final static Logger logger = Logger.getLogger(DrawMeasure.class);

    public LineChart glucoChart;
    public StackPane stackPane;
    public AnchorPane legendPane;
    public MenuBar menuBar;
    public NumberAxis xAxis;
    public NumberAxis yAxis;
    public Label legendLabel;
    public ChoiceBox graphChoice;
    public ColorPicker colorChoice;
    public Label descriptionLabel;
    public LineChart visualPlot;
    public NumberAxis xTimeVisual;
    public NumberAxis yAmpVisual;
    public Button deleteSeriesButton;

    MultipleSameAxesLineChart multipleAxesLineChart;
    Map<XYChart.Series, MeasureMode> chartDataMap = new HashMap<>();

    public void addFirstSeries(String seriesName, MeasureMode mode) {
        multipleAxesLineChart = new MultipleSameAxesLineChart(glucoChart, stackPane);
        if (mode.getClass().getName().equals(MeasurementSetup.class.getName())) {
            MeasurementSetup measurementSetup = (MeasurementSetup) mode;
            XYChart.Series series = VisualisationPlot.prepareSeries(seriesName, measurementSetup.getData());
            multipleAxesLineChart.addSeries(series, Color.RED, seriesName, measurementSetup);
            chartDataMap.put(series, measurementSetup);
            showDetails();
        } else if (mode.getClass().getName().equals(PolySetup.class.getName())) {
            PolySetup polySetup = (PolySetup) mode;
            XYChart.Series series = VisualisationPlot.prepareSeries(seriesName, polySetup.getData());
            multipleAxesLineChart.addSeries(series, Color.RED, seriesName, polySetup);
            chartDataMap.put(series, polySetup);
            showDetails();
        }
    }

    void showDetails() {
        graphChoice.getItems().clear();
        for (int i = 0; i < glucoChart.getData().size(); i++) {
            graphChoice.getItems().add(glucoChart.getData().get(i));
        }
        graphChoice.getSelectionModel().select(0);
        XYChart.Series series = (XYChart.Series) graphChoice.getValue();
        Color color = multipleAxesLineChart.getSeriesColor(series);
        colorChoice.setValue(color);
    }

    void menuBarSetup() {
        menuBar.getMenus().clear();

        SeparatorMenuItem separator = new SeparatorMenuItem();

        Menu file = new Menu("Файл");

        MenuItem open = new MenuItem("Добавить");
        MenuItem close = new MenuItem("Выход");
        open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        close.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        open.setOnAction((event) -> {
//            try {
//                openPlotWindow();
//            } catch (IOException e) {
//                logger.error(e.getMessage());
//                e.printStackTrace();
//            }
        });
        close.setOnAction((event) -> {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.close();
        });

        file.getItems().addAll(/*open, */separator, close);

        menuBar.getMenus().addAll(file);
    }

    void deleteSerie(String choosedSeriesName) {
        for (int i = 0; i < glucoChart.getData().size(); i++) {
            XYChart.Series series = (XYChart.Series) glucoChart.getData().get(i);
            if (series.getName().equals(choosedSeriesName)) {
                chartDataMap.remove(series);
                glucoChart.getData().clear();
                for (Map.Entry<XYChart.Series, MeasureMode> entry : chartDataMap.entrySet()) {
                    XYChart.Series series1 = entry.getKey();
                    multipleAxesLineChart.addSeries(
                            series1,
                            multipleAxesLineChart.getSeriesColor(series1),
                            series1.getName(),
                            multipleAxesLineChart.getChartData(series1));
                }
            }
        }
        graphChoice.getSelectionModel().select(0);
    }

    void drawGraphics(ObservableList<String> selectedItems, String rootPath) {
        multipleAxesLineChart = new MultipleSameAxesLineChart(glucoChart, stackPane);
        if (rootPath.equals(Write.fileMeasurePath)) {
            for (String seriesName : selectedItems) {
                MeasureMode mode = Read.reading(rootPath + "\\" + seriesName);
                MeasurementSetup measurementSetup = (MeasurementSetup) mode;
                XYChart.Series series = VisualisationPlot.prepareSeries(seriesName, measurementSetup.getData());
                Color color = Color.color(Math.random(),0, Math.random());
                multipleAxesLineChart.addSeries(series, color, seriesName, measurementSetup);
                chartDataMap.put(series, measurementSetup);
                showDetails();
            }
        } else if (rootPath.equals(Write.filePolyPath)) {
            for (String seriesName : selectedItems) {
                MeasureMode mode = Read.reading(rootPath + "\\" + seriesName);
                PolySetup polySetup = (PolySetup) mode;
                XYChart.Series series = VisualisationPlot.prepareSeries(seriesName, polySetup.getData());
                Color color = Color.color(Math.random(),0, Math.random());
                multipleAxesLineChart.addSeries(series, color, seriesName, polySetup);
                chartDataMap.put(series, polySetup);
                showDetails();
            }
        }

    }

}
