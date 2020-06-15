package com.controllers;

import com.entity.MeasureMode;
import com.entity.MeasurementSetup;
import com.file.ChooseFile;
import com.file.Read;
import com.file.Write;
import com.graph.MultipleSameAxesLineChart;
import com.graph.VisualisationPlot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

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
    }

    public void setSecondDotOnChart(ActionEvent actionEvent) {
        int secondDot = Integer.parseInt(secondDotEdit.getText());
        setDotOnChart("two", 2, secondDot);
        createAdditionalLines("additionalTwo", secondDot);
    }

    public void setThirdDotOnChart(ActionEvent actionEvent) {
        int thirdDot = Integer.parseInt(thirdDotEdit.getText());
        setDotOnChart("tree", 3, thirdDot);
        createAdditionalLines("additionalThree", thirdDot);
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
                            setLabel(label, columnIndex, i + 2, yValues, workIndex);
                        }
                    }
                }
            }
        }
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
        for (int i = 2; i <= 11; i++) {
            Label label = new Label();
            label.setText("");
            label.setId(columnName + columnIndex + "-" + i);
            gridPane.add(label, columnIndex, i);
        }
    }

    private void setLabel(Label label, int columnIndex, int iteration, ArrayList<Number> yValues, int workIndex) {
        if (label.getId().contains(columnIndex + "-" + iteration)) {
            label.setText("");
            logger.debug(yValues.get(workIndex));
            label.setText(String.valueOf(yValues.get(workIndex)));
        }
    }
}
