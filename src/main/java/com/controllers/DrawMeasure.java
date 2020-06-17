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
import java.util.ArrayList;
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
    public CheckBox onlyMeasurementCheckBox;

    MultipleSameAxesLineChart multipleAxesLineChart;
    Map<XYChart.Series, MeasureMode> chartDataMap = new HashMap<>();

    static final public Color[] colors = {
            Color.color(0, 0, 0),
            Color.color(0.094, 0.12, 0.97),
            Color.color(0.81, 0.14, 0.07),
            Color.color(0.19, 0.74, 0),
            Color.color(1, 0.06, 0.85),
            Color.color(0.84, 0.83, 0.07),
            Color.color(0.67, 0.54, 0.56),
            Color.color(0.47, 0.11, 0.63),
            Color.color(0.31, 0.69, 0),
            Color.color(0.30, 0.98, 0.06)
    };

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
            XYChart.Series series = (XYChart.Series) glucoChart.getData().get(i);
            graphChoice.getItems().add(series.getName());
        }
        graphChoice.getSelectionModel().select(0);
        XYChart.Series series = getSeriesByName((String) graphChoice.getValue());
        //XYChart.Series series = (XYChart.Series) graphChoice.getValue();
        Color color = multipleAxesLineChart.getSeriesColor(series);
        colorChoice.setValue(color);
    }

    XYChart.Series getSeriesByName(String seriesName) {
        seriesName = (String) graphChoice.getValue();
        XYChart.Series series = null;
        for (int i = 0; i < glucoChart.getData().size(); i++) {
            series = (XYChart.Series) glucoChart.getData().get(i);
            if (series.getName().equals(seriesName)) break;
        }
        return series;
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
        if (rootPath.contains(Write.fileMeasurePath)) {
            for (int i = 0; i < selectedItems.size(); i++) {
                String seriesName = selectedItems.get(i);
                logger.debug(rootPath + "\\" + seriesName);
                MeasureMode mode = Read.reading(rootPath + "\\" + seriesName);
                MeasurementSetup measurementSetup = (MeasurementSetup) mode;
                XYChart.Series series = VisualisationPlot.prepareSeries(seriesName, measurementSetup.getData());
                multipleAxesLineChart.addSeries(series, colors[i], seriesName, measurementSetup);
                chartDataMap.put(series, measurementSetup);
                showDetails();
            }
        } else if (rootPath.contains(Write.filePolyPath)) {
            for (int i = 0; i < selectedItems.size(); i++) {
                String seriesName = selectedItems.get(i);
                MeasureMode mode = Read.reading(rootPath + "\\" + seriesName);
                PolySetup polySetup = (PolySetup) mode;
                XYChart.Series series = VisualisationPlot.prepareSeries(seriesName, polySetup.getData());
                multipleAxesLineChart.addSeries(series, colors[i], seriesName, polySetup);
                chartDataMap.put(series, polySetup);
                showDetails();
            }
        }

    }

}
