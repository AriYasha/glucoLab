package com.controllers;

import com.entity.Data;
import com.entity.MeasureMode;
import com.entity.MeasurementSetup;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GraphController extends DrawMeasure implements Initializable {

    final static Logger logger = Logger.getLogger(GraphController.class);
    //private final Map<XYChart.Series, MeasurementSetup> chartDataMap = new HashMap<>();

   // public LineChart glucoChart;
   // public StackPane stackPane;
//    public AnchorPane legendPane;
//    public MenuBar menuBar;
//    public NumberAxis xAxis;
//    public NumberAxis yAxis;
//    public Label legendLabel;
//    public ChoiceBox graphChoice;
//    public ColorPicker colorChoice;
//    public Label descriptionLabel;
//    public LineChart visualPlot;
//    public NumberAxis xTimeVisual;
//    public NumberAxis yAmpVisual;
//    public Button deleteSeriesButton;

    private String fileName;

    //MultipleSameAxesLineChart multipleAxesLineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBarSetup();
        graphChoice.setOnAction((event) -> seriesChooser());
        deleteSeriesButton.setBackground(null);
        glucoChart.setAnimated(false);
//        createMultiAxesLineChart();
//        multipleAxesLineChart.addSeries(prepareSeries(fileName, firstData), Color.color(Math.random(), 0, Math.random()), fileName);

    }

//    public void addFirstSeries(String seriesName, MeasurementSetup measurementSetup){
//        multipleAxesLineChart = new MultipleSameAxesLineChart(glucoChart, stackPane);
//        glucoChart.setAnimated(false);
//        //graphController.glucoChart.getData().add(currentSeries);
//        XYChart.Series series = VisualisationPlot.prepareSeries(seriesName, measurementSetup.getData());
//        multipleAxesLineChart.addSeries(series, Color.RED, seriesName, measurementSetup.getData());
//        chartDataMap.put(series, measurementSetup);
////        legendLabel.setText(data.getMeasurementSetup().toString());
//        //graphController.legendPane.getChildren().add(currentSeries.getLegend());
//        showDetails();
//    }

//    private void showDetails(){
//        graphChoice.getItems().clear();
//        for (int i = 0; i < glucoChart.getData().size(); i++) {
//            graphChoice.getItems().add(glucoChart.getData().get(i));
//        }
//        graphChoice.getSelectionModel().select(0);
//        XYChart.Series series = (XYChart.Series) graphChoice.getValue();
//        Color color = multipleAxesLineChart.getSeriesColor(series);
//        colorChoice.setValue(color);
//    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

//    private void menuBarSetup() {
//        menuBar.getMenus().clear();
//
//        SeparatorMenuItem separator = new SeparatorMenuItem();
//
//        Menu file = new Menu("Файл");
//
//        MenuItem open = new MenuItem("Добавить");
//        MenuItem close = new MenuItem("Выход");
//        open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
//        close.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
//        open.setOnAction((event) -> {
//            try {
//                openPlotWindow();
//            } catch (IOException e) {
//                logger.error(e.getMessage());
//                e.printStackTrace();
//            }
//        });
//        close.setOnAction((event) -> {
//            Stage stage = (Stage) menuBar.getScene().getWindow();
//            stage.close();
//        });
//
//        file.getItems().addAll(open, separator, close);
//
//        menuBar.getMenus().addAll(file);
//    }

    private void openPlotWindow() throws IOException {
        String fileName = ChooseFile.chooseFile(Write.fileMeasurePath);
        MeasurementSetup measurementSetup = Read.reading1(Write.fileMeasurePath + "\\" + fileName);
        XYChart.Series series = VisualisationPlot.prepareSeries(fileName, measurementSetup.getData());
        multipleAxesLineChart.addSeries(
                series,
                Color.color(Math.random(),0, Math.random()),
                fileName,
                measurementSetup
        );
        chartDataMap.put(series, measurementSetup);
        showDetails();
    }

    private void createMultiAxesLineChart() {
        multipleAxesLineChart = new MultipleSameAxesLineChart(glucoChart, stackPane);
    }

    public void seriesChooser() {
        XYChart.Series series = (XYChart.Series) graphChoice.getValue();
        Color color = multipleAxesLineChart.getSeriesColor(series);
        colorChoice.setValue(color);
        setDescriptionLabel(multipleAxesLineChart.getChartData(series));
        VisualisationPlot visualisationPlot = new VisualisationPlot(visualPlot);
        if((MeasurementSetup)chartDataMap.get(series) != null) {
            visualisationPlot.drawMeasureGraphic(visualPlot, (MeasurementSetup) chartDataMap.get(series));
        } else {
            visualPlot.getData().clear();
        }
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

    private void setDescriptionLabel(MeasureMode mode){
        if(mode != null) {
//        descriptionLabel.setText("Описание выбранного графика :\n" +
//                "\nТип полоски :\n\t" + data.getStripType() +
//                "\nВремя протекания :\n\t" + data.getMeasurementSetup().getLeakingTime() + ", мс" +
//                "\nВремя паузы :\n\t" + data.getMeasurementSetup().getPauseTime() + ", мс" +
//                "\nКоличество импульсов ПП :\n\t" + data.getMeasurementSetup().getQuantityFastPolarityPulses() +
//                "\nАмплитуда положительных импульсов ПП :\n\t" + data.getMeasurementSetup().getPositiveAmplitudeFastPolarityPulses() + ", мВ" +
//                "\nАмплитуда отрицательных импульсов ПП :\n\t" + data.getMeasurementSetup().getNegativeAmplitudeFastPolarityPulses() +
//                "\nВремя отрицательных импульсов ПП :\n\t" + data.getMeasurementSetup().getNegativeFastPolarityReversalTime() + ", мс" +
//                "\nВремя положительных импульсов ПП :\n\t" + data.getMeasurementSetup().getPositiveFastPolarityReversalTime() + ", мс" +
//                "\nАмплитуда положительного импульса измерения :\n\t" + data.getMeasurementSetup().getNegativeAmplitudeMeasurePulses() + ", мВ" +
//                "\nАмплитуда отрицательного импульса измерения :\n\t" + data.getMeasurementSetup().getPositiveAmplitudeMeasurePulses() + ", мВ" +
//                "\nВремя отрицательного импульса измерения :\n\t" + data.getMeasurementSetup().getNegativeMeasureTime() + ", мс" +
//                "\nВремя положительного импульса измерения :\n\t" + data.getMeasurementSetup().getPositiveMeasureTime() + ", мс" +
//                ""
//        );
            descriptionLabel.setText(((MeasurementSetup) mode).toString());
        } else {
            descriptionLabel.setText("Описание выбранного графика :\n");
        }
    }

    public void deleteSeries(ActionEvent actionEvent) {
        String choosedSeriesName = ((XYChart.Series) graphChoice.getValue()).getName();
        graphChoice.getItems().remove(graphChoice.getValue());
        deleteSerie(choosedSeriesName);
//        String choosedSeriesName = ((XYChart.Series) graphChoice.getValue()).getName();
//        graphChoice.getItems().remove(graphChoice.getValue());
//        for (int i = 0; i < glucoChart.getData().size(); i++) {
//            XYChart.Series series = (XYChart.Series) glucoChart.getData().get(i);
//            if (series.getName().equals(choosedSeriesName)) {
//                chartDataMap.remove(series);
//                glucoChart.getData().clear();
//                for (Map.Entry<XYChart.Series, MeasureMode> entry : chartDataMap.entrySet()) {
//                    XYChart.Series series1 = entry.getKey();
//                    multipleAxesLineChart.addSeries(
//                            series1,
//                            multipleAxesLineChart.getSeriesColor(series1),
//                            series1.getName(),
//                            multipleAxesLineChart.getChartData(series1));
//                }
//            }
//        }
//        graphChoice.getSelectionModel().select(0);
    }
}
