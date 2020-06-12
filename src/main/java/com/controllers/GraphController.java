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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GraphController extends DrawMeasure implements Initializable {

    final static Logger logger = Logger.getLogger(GraphController.class);
    public GridPane gridPane;
    public TextField firstDotEdit;
    public TextField secondDotEdit;
    public TextField ThirdDotEdit;
    public Label graphOneLabel;
    public Label graphTwoLabel;
    public Label graphThreeLabel;
    public Label graphSixLabel;
    public Label graphFiveLabel;
    public Label graphFourLabel;
    public Label graphNineLabel;
    public Label graphEightLabel;
    public Label graphSevenLabel;
    public Label graphTenLabel;
    private String setValueString = "Установите значения";
    private ObservableList<String> descriptionValues = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBarSetup();
        graphChoice.setOnAction((event) -> seriesChooser());
        deleteSeriesButton.setBackground(null);
        glucoChart.setAnimated(false);
        descriptionValues.add(setValueString);

    }

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
        XYChart.Series series = getSeriesByName((String) graphChoice.getValue());
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
        XYChart.Series series = getSeriesByName((String) graphChoice.getValue());
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
        String choosedSeriesName = (String) graphChoice.getValue();
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

    public void setFirstDotOnChart(ActionEvent actionEvent) {
        int firstDot = Integer.parseInt(firstDotEdit.getText());
    }

    public void setSecondDotOnChart(ActionEvent actionEvent) {
    }

    public void setThirdDotOnChart(ActionEvent actionEvent) {
    }
}
