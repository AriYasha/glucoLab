package com.controllers;

import com.calculations.Integral;
import com.entity.MeasureMode;
import com.entity.MeasurementSetup;
import com.file.ChooseFile;
import com.file.Read;
import com.file.Write;
import com.graph.MultipleAxesLineChart;
import com.graph.MultipleSameAxesLineChart;
import com.graph.VisualisationPlot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GraphController extends DrawMeasure implements Initializable {

    final static Logger logger = Logger.getLogger(GraphController.class);
    public GridPane gridPane;
    public TextField firstDotEdit;
    public TextField secondDotEdit;
    public TextField thirdDotEdit;
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
    public Label deviationOneLabel;
    public Label deviationTwoLabel;
    public Label deviationThreeLabel;
    public Label negativeLabel;
    public Label positiveLabel;
    public Label customLabel;
    public CheckBox showVoltageCheckBox;
    public Label variationOne;
    public Label variationTwo;
    public Label variationThree;
    private String setValueString = "Установите значения";
    private ObservableList<String> descriptionValues = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBarSetup();
        graphChoice.setOnAction((event) -> seriesChooser());
        deleteSeriesButton.setBackground(null);
        glucoChart.setAnimated(false);
        descriptionValues.add(setValueString);
        positiveLabel.setAlignment(Pos.CENTER);
        negativeLabel.setAlignment(Pos.CENTER);
        customLabel.setAlignment(Pos.CENTER);

    }

    private void openPlotWindow() throws IOException {
        String fileName = ChooseFile.chooseFile(Write.fileMeasurePath);
        MeasurementSetup measurementSetup = Read.reading1(Write.fileMeasurePath + "\\" + fileName);
        XYChart.Series series = VisualisationPlot.prepareSeries(fileName, measurementSetup.getData());
        multipleAxesLineChart.addSeries(
                series,
                Color.color(Math.random(), 0, Math.random()),
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
        if ((MeasurementSetup) chartDataMap.get(series) != null) {
            visualisationPlot.drawMeasureGraphic(visualPlot, (MeasurementSetup) chartDataMap.get(series));
        } else {
            visualPlot.getData().clear();
        }

        ObservableList<XYChart.Data> dataFromPlot = series.getData();
        ArrayList<Integer> xValues = new ArrayList<>();
        ArrayList<Number> xValuesForClosest = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();
        for (XYChart.Data newData : dataFromPlot) {
            xValues.add((Integer) newData.getXValue());
            xValuesForClosest.add((Number) newData.getXValue());
            yValues.add((Float) newData.getYValue());
        }
        getCustomIntegral();
        positiveLabel.setText(String.valueOf(Integral.getPositiveIntegral(xValues, yValues)));
        negativeLabel.setText(String.valueOf(Integral.getNegativeIntegral(xValues, yValues)));

        showVoltage(new ActionEvent());

    }

    private void getCustomIntegral() {
        XYChart.Series series = getSeriesByName((String) graphChoice.getValue());
        ObservableList<XYChart.Data> dataFromPlot = series.getData();
        ArrayList<Integer> xValues = new ArrayList<>();
        ArrayList<Number> xValuesForClosest = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<>();
        for (XYChart.Data newData : dataFromPlot) {
            xValues.add((Integer) newData.getXValue());
            xValuesForClosest.add((Number) newData.getXValue());
            yValues.add((Float) newData.getYValue());
        }
        if (!firstDotEdit.getText().isEmpty() && !secondDotEdit.getText().isEmpty()) {
            int firstDot = Integer.parseInt(firstDotEdit.getText());
            int secondDot = Integer.parseInt(secondDotEdit.getText());
            int beginIndex = getCloseIndex(xValuesForClosest, firstDot);
            int endIndex = getCloseIndex(xValuesForClosest, secondDot);
            try {
                customLabel.setText(String.valueOf(Integral.getCustomIntegral(xValues, yValues, beginIndex, endIndex)));
            } catch (RuntimeException e) {
                logger.debug(e);
                customLabel.setText("");
            }

        } else {
            customLabel.setText("");
        }
    }

    public void setGraphColor(ActionEvent actionEvent) {
        XYChart.Series series = getSeriesByName((String) graphChoice.getValue());
        Color color = colorChoice.getValue();
        multipleAxesLineChart.setColor(series, color);
        for (int i = 0; i < glucoChart.getData().size(); i++) {
            XYChart.Series currentSeries = (XYChart.Series) glucoChart.getData().get(i);
            if (currentSeries.equals(series)) {
                multipleAxesLineChart.styleChartLine(currentSeries, color);
            }
        }

    }

    private void setDescriptionLabel(MeasureMode mode) {
        if (mode != null) {
            descriptionLabel.setText(((MeasurementSetup) mode).toString());
        } else {
            descriptionLabel.setText("Описание выбранного графика :\n");
        }
    }

    public void deleteSeries(ActionEvent actionEvent) {
//        String choosedSeriesName = (String) graphChoice.getValue();
//        graphChoice.getItems().remove(graphChoice.getValue());
//        deleteSerie(choosedSeriesName);

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
        setDotOnChart("one", 1, firstDot);
        createAdditionalLines("additionalOne", firstDot);
        getCustomIntegral();
    }

    public void setSecondDotOnChart(ActionEvent actionEvent) {
        int secondDot = Integer.parseInt(secondDotEdit.getText());
        setDotOnChart("two", 2, secondDot);
        createAdditionalLines("additionalTwo", secondDot);
        getCustomIntegral();
    }

    public void setThirdDotOnChart(ActionEvent actionEvent) {
        int thirdDot = Integer.parseInt(thirdDotEdit.getText());
        setDotOnChart("tree", 3, thirdDot);
        createAdditionalLines("additionalThree", thirdDot);
    }

    public void setFirstDotOnChartM(MouseEvent mouseEvent) {
        String first = firstDotEdit.getText();
        if (!first.isEmpty()) {
            int firstDot = Integer.parseInt(firstDotEdit.getText());
            setDotOnChart("one", 1, firstDot);
            createAdditionalLines("additionalOne", firstDot);
            getCustomIntegral();
        }
    }

    public void setSecondDotOnChartM(MouseEvent mouseEvent) {
        String second = firstDotEdit.getText();
        if (!second.isEmpty()) {
            int secondDot = Integer.parseInt(secondDotEdit.getText());
            setDotOnChart("two", 2, secondDot);
            createAdditionalLines("additionalTwo", secondDot);
            getCustomIntegral();
        }
    }

    public void setThirdDotOnChartM(MouseEvent mouseEvent) {
        String third = firstDotEdit.getText();
        if (!third.isEmpty()) {
            int thirdDot = Integer.parseInt(thirdDotEdit.getText());
            setDotOnChart("tree", 3, thirdDot);
            createAdditionalLines("additionalThree", thirdDot);
        }
    }

    private void createAdditionalLines(String lineName, int dot) {
        for (int i = 0; i < glucoChart.getData().size(); i++) {
            XYChart.Series series = (XYChart.Series) glucoChart.getData().get(i);
            if (series.getName().contains(lineName)) {
                glucoChart.getData().remove(series);
            }
        }
        XYChart.Series series = new XYChart.Series();
        Double minValue = yAxis.lowerBoundProperty().getValue();
        Double maxValue = yAxis.upperBoundProperty().getValue();
        series.getData().add(new XYChart.Data<>(dot, minValue + 2));
        series.getData().add(new XYChart.Data<>(dot, maxValue - 2));
        series.setName(lineName);
        glucoChart.getData().add(series);
        Node line = series.getNode().lookup(".chart-series-line");
        line.setStyle("-fx-stroke-width: 1; -fx-stroke: #003b3d; -fx-stroke-dash-array: 5 10 10 5;");
    }

    private void setDotOnChart(String columnName, int columnIndex, int dot) {
        setLabels(columnName, columnIndex);
        List<Float> deviationList = new ArrayList<>();
        for (int i = 0; i < glucoChart.getData().size(); i++) {
            XYChart.Series series = (XYChart.Series) glucoChart.getData().get(i);
            if (!series.getName().contains("additional")) {
                ObservableList<XYChart.Data> dataFromPlot = series.getData();
                ArrayList<Number> xValues = new ArrayList<>();
                ArrayList<Number> yValues = new ArrayList<>();
                for (XYChart.Data newData : dataFromPlot) {
                    xValues.add((Number) newData.getXValue());
                    yValues.add((Number) newData.getYValue());
                }
                int workIndex = getCloseIndex(xValues, dot);
                logger.debug(xValues.size());
                logger.debug(yValues.size());
                logger.debug(workIndex);
                for (Node node : gridPane.getChildren()) {
                    if (node.getClass().equals(Label.class)) {
                        Label label = (Label) node;
                        if (label.getId() != null && label.getId().contains(columnName)) {
                            deviationList = setLabel(label, columnIndex, i + 2, deviationList, yValues.get(workIndex));
                        }
                    }
                }
            }
        }
        logger.debug(deviationList.toString());
        logger.debug(standardDeviation(deviationList));
        setDeviationLabel(columnIndex, standardDeviation(deviationList));
        setVariationLabel(columnIndex, variation(deviationList));
        System.out.println("variation = " + variation(deviationList));
    }

    private float variation(List<Float> deviationList) {
        float mean = 0;
        for (float digit : deviationList) {
            mean += digit;
        }
        mean /= deviationList.size();
        return (standardDeviation(deviationList) / mean) * 100;
    }

    private float standardDeviation(List<Float> deviationList) {
        float mean = 0;
        for (float digit : deviationList) {
            mean += digit;
        }
        mean /= deviationList.size();
        logger.debug(mean);
        float sum = 0;
        for (Float aDeviationList : deviationList) {
            sum += Math.pow(aDeviationList - mean, 2);
        }
        logger.debug(sum);
        float standardDeviation = (float) Math.sqrt(sum / (deviationList.size()));
        return standardDeviation;
    }

    private int getCloseIndex(ArrayList<Number> list, int a) {
        int right = list.size();
        int left = 0;
        int indexFinally = 0;

        while (right - left > 1) {
            int mid = (left + right) / 2;
            if ((Integer) list.get(mid) > a) {
                right = mid;
            } else {
                left = mid;
            }
        }
        if (a <= (int) list.get(list.size() - 1)) {
            if ((Integer) list.get(right) == a) {
                indexFinally = right;
            }
            if ((Integer) list.get(left) == a) {
                indexFinally = left;
            } else if (Math.abs((Integer) list.get(right) - a) > Math.abs((Integer) list.get(left) - a) ||
                    Math.abs((Integer) list.get(right) - a) == Math.abs((Integer) list.get(left) - a)) {
                indexFinally = right;
            } else {
                indexFinally = left;
            }
        } else {
            indexFinally = list.size() - 1;
        }

        return indexFinally;
    }

    private void setLabels(String columnName, int columnIndex) {
        for (int i = 2; i <= 12; i++) {
            Label label = new Label();
            label.setText("");
            label.setAlignment(Pos.CENTER);
            label.setId(columnName + columnIndex + "-" + i);
            gridPane.add(label, columnIndex, i);
        }
    }

    private List<Float> setLabel(Label label, int columnIndex, int iteration, List<Float> deviationList, Number value) {
        if (label.getId().contains(columnIndex + "-" + iteration)) {
            label.setAlignment(Pos.CENTER);
            label.setText("");
            logger.debug(value);
            label.setText(String.valueOf(value));
            deviationList.add((Float) value);
        }
        return deviationList;
    }

    private void setDeviationLabel(int columnIndex, Number value) {
        switch (columnIndex) {
            case 1:
                deviationOneLabel.setText("");
                logger.debug(value);
                deviationOneLabel.setText(String.valueOf(value));
                break;
            case 2:
                deviationTwoLabel.setText("");
                logger.debug(value);
                deviationTwoLabel.setText(String.valueOf(value));
                break;
            case 3:
                deviationThreeLabel.setText("");
                logger.debug(value);
                deviationThreeLabel.setText(String.valueOf(value));
                break;
        }
    }

    private void setVariationLabel(int columnIndex, Number value) {
        switch (columnIndex) {
            case 1:
                variationOne.setText("");
                logger.debug(value);
                variationOne.setText(String.valueOf(value));
                break;
            case 2:
                variationTwo.setText("");
                logger.debug(value);
                variationTwo.setText(String.valueOf(value));
                break;
            case 3:
                variationThree.setText("");
                logger.debug(value);
                variationThree.setText(String.valueOf(value));
                break;
        }
    }

    public void onlyMeasurement(ActionEvent actionEvent) {
        if (onlyMeasurementCheckBox.isSelected()) {
            xAxis.setAutoRanging(false);
            yAxis.setAutoRanging(false);
            xAxis.setLowerBound(0);
        } else {
            xAxis.setAutoRanging(true);
            yAxis.setAutoRanging(true);
        }
    }

    public void showVoltage(ActionEvent actionEvent) {
        if (showVoltageCheckBox.isSelected()) {
            String seriesName = (String) graphChoice.getValue();
            String rootPath = Write.fileMeasurePath;
            MeasureMode mode = Read.reading(rootPath + "\\" + seriesName);
            MeasurementSetup measurementSetup = (MeasurementSetup) mode;
            if (measurementSetup != null) {
                XYChart.Series voltageSeries = VisualisationPlot.prepareVoltageSeries(seriesName, measurementSetup.getData());
                voltageSeries.setName("Напряжение");
                MultipleAxesLineChart voltageChart = new MultipleAxesLineChart(glucoChart, stackPane);
                voltageChart.addSeries(voltageSeries, Color.RED);
                legendPane.getChildren().add(voltageChart.getLegend());
            }
        } else {
            logger.debug(stackPane.getChildren().size());
            for (int i = 0; i < stackPane.getChildren().size(); i++) {
                if(stackPane.getChildren().get(i).getClass().equals(HBox.class)){
                    stackPane.getChildren().remove(i);
                }
            }
            logger.debug(stackPane.getChildren().toString());
        }
    }
}
