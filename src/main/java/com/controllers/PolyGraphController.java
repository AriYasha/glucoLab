package com.controllers;

import com.entity.Data;
import com.entity.PolySetup;
import com.file.ChooseFile;
import com.file.Read;
import com.file.Write;
import com.graph.MultipleSameAxesLineChart;
import com.graph.VisualisationPlot;
import javafx.event.ActionEvent;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PolyGraphController implements Initializable {

    final static Logger logger = Logger.getLogger(PolyGraphController.class);
    private final Map<XYChart.Series, PolySetup> chartDataMap = new HashMap<>();

    public AnchorPane legendPane;
    public Label legendLabel;
    public StackPane stackPane;
    public LineChart glucoChart;
    public NumberAxis yAxis;
    public NumberAxis xAxis;
    public MenuBar menuBar;
    public ColorPicker colorChoice;
    public ChoiceBox graphChoice;
    public Label descriptionLabel;
    public LineChart visualPlot;
    public NumberAxis xTimeVisual;
    public NumberAxis yAmpVisual;
    public Button deleteSeriesButton;

    MultipleSameAxesLineChart multipleAxesLineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBarSetup();
        deleteSeriesButton.setBackground(null);
        graphChoice.setOnAction((event) -> seriesChooser());
        glucoChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
    }

    public void addFirstSeries(String fileName, PolySetup polySetup) {
        multipleAxesLineChart = new MultipleSameAxesLineChart(glucoChart, stackPane);
        glucoChart.setAnimated(false);
        //graphController.glucoChart.getData().add(currentSeries);
        XYChart.Series series = VisualisationPlot.prepareSeries(fileName, polySetup.getData());
        multipleAxesLineChart.addSeries(series, Color.RED, fileName, polySetup.getData());
        chartDataMap.put(series, polySetup);
//        legendLabel.setText(data.getMeasurementSetup().toString());
        //graphController.legendPane.getChildren().add(currentSeries.getLegend());
        showDetails();
    }

    private void showDetails(){
        graphChoice.getItems().clear();
        for (int i = 0; i < glucoChart.getData().size(); i++) {
            graphChoice.getItems().add(glucoChart.getData().get(i));
        }
        graphChoice.getSelectionModel().select(0);
        XYChart.Series series = (XYChart.Series) graphChoice.getValue();
        Color color = multipleAxesLineChart.getSeriesColor(series);
        colorChoice.setValue(color);
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
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.close();
        });

        file.getItems().addAll(open, separator, close);

        menuBar.getMenus().addAll(file);
    }

    private void openPlotWindow() throws IOException {
        String fileName = ChooseFile.chooseFile(Write.filePolyPath);
        PolySetup polySetup = Read.readingPoly(Write.filePolyPath + "\\" + fileName);
        XYChart.Series series = VisualisationPlot.prepareSeries(fileName, polySetup.getData());
        multipleAxesLineChart.addSeries(
                series,
                Color.color(Math.random(),0, Math.random()),
                fileName,
                polySetup.getData()
        );
        chartDataMap.put(series, polySetup);
        showDetails();
    }

    public void seriesChooser() {
        XYChart.Series series = (XYChart.Series) graphChoice.getValue();
        Color color = multipleAxesLineChart.getSeriesColor(series);
        colorChoice.setValue(color);
        setDescriptionLabel(chartDataMap.get(series));
        VisualisationPlot visualisationPlot = new VisualisationPlot(visualPlot);
        visualisationPlot.drawPolyGraphic(visualPlot, chartDataMap.get(series));
    }

    public void setGraphColor(ActionEvent actionEvent) {
        XYChart.Series series = (XYChart.Series) graphChoice.getValue();
        Color color = colorChoice.getValue();
        multipleAxesLineChart.setColor(series, color);
        for (int i = 0; i < glucoChart.getData().size(); i++) {
            XYChart.Series currentSeries = (XYChart.Series) glucoChart.getData().get(i);
            if(currentSeries.equals(series)){
                multipleAxesLineChart.styleChartLine(currentSeries, color);
            }
        }
    }

    private void setDescriptionLabel(PolySetup polySetup){
        descriptionLabel.setText("Описание выбранного графика :\n" +
                "\nТип полоски :\n\t" + polySetup.getData().getStripType() +
                "\nАмплитуда начальной точки :\n\t" + polySetup.getBeginPoint() + ", мВ" +
                "\nАмплитуда средней точки :\n\t" + polySetup.getMediumPoint() + ", мВ" +
                "\nАмплитуда конечной точки :\n\t" + polySetup.getLastPoint() + ", мВ" +
                "\nВремя нарастания :\n\t" + polySetup.getIncreaseTime() + ", мс" +
                "\nВремя спада :\n\t" + polySetup.getDecreaseTime() + ", мс" +
                "\nКоличество повторений :\n\t" + polySetup.getQuantityReapeted() +
                ""
        );
    }

    public void deleteSeries(ActionEvent actionEvent) {
        String choosedSeriesName = ((XYChart.Series) graphChoice.getValue()).getName();
        graphChoice.getItems().remove(graphChoice.getValue());
        for (int i = 0; i < glucoChart.getData().size(); i++) {
            XYChart.Series series = (XYChart.Series) glucoChart.getData().get(i);
            if (series.getName().equals(choosedSeriesName)) {
                chartDataMap.remove(series);
                glucoChart.getData().clear();
                for (Map.Entry<XYChart.Series, PolySetup> entry : chartDataMap.entrySet()) {
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
}
