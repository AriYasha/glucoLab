package com.controllers;

import com.entity.Data;
import com.file.ChooseFile;
import com.file.Read;
import com.graph.MultipleSameAxesLineChart;
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
    public ChoiceBox graphChoice;
    public ColorPicker colorChoice;
    public Label descriptionLabel;

    private Data firstData;
    private String fileName;

    private boolean isOpen = false;

    MultipleSameAxesLineChart multipleAxesLineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBarSetup();
        graphChoice.setOnAction((event) -> seriesChooser());

//        createMultiAxesLineChart();
//        multipleAxesLineChart.addSeries(prepareSeries(fileName, firstData), Color.color(Math.random(), 0, Math.random()), fileName);

    }

    public void addFirstSeries(String seriesName, Data data){
        multipleAxesLineChart = new MultipleSameAxesLineChart(glucoChart, stackPane);
        glucoChart.setAnimated(false);
        //graphController.glucoChart.getData().add(currentSeries);
        multipleAxesLineChart.addSeries(prepareSeries(seriesName, data), Color.RED, fileName, data);
        legendLabel.setText(data.getMeasurementSetup().toString());
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
//        if (!isOpen) {
//            createMultiAxesLineChart();
//        }
        multipleAxesLineChart.addSeries(prepareSeries(fileName, data), Color.color(Math.random(), 0, Math.random()), fileName, data);
        legendLabel.setText(data.getMeasurementSetup().toString());
        //legendPane.getChildren().add(multipleAxesLineChart.getLegend());
        //glucoChart.getData().add(prepareSeries(fileName, data));
        showDetails();
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

    public void seriesChooser() {
        XYChart.Series series = (XYChart.Series) graphChoice.getValue();
        Color color = multipleAxesLineChart.getSeriesColor(series);
        colorChoice.setValue(color);
        setDescriptionLabel(multipleAxesLineChart.getChartData(series));
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

    private void setDescriptionLabel(Data data){
        System.out.println(data.toString());
        descriptionLabel.setText("Описание выбранного графика :\n" +
                "\nТип полоски :\n\t" + data.getStripType() +
                "\nВремя протекания :\n\t" + data.getMeasurementSetup().getLeakingTime() +
                "\nВремя паузы :\n\t" + data.getMeasurementSetup().getPauseTime() +
                "\nКоличество импульсов ПП :\n\t" + data.getMeasurementSetup().getQuantityFastPolarityPulses() +
                "\nАмплитуда положительных импульсов ПП :\n\t" + data.getMeasurementSetup().getPositiveAmplitudeFastPolarityPulses() +
                "\nАмплитуда отрицательных импульсов ПП :\n\t" + data.getMeasurementSetup().getNegativeAmplitudeFastPolarityPulses() +
                "\nВремя отрицательных импульсов ПП :\n\t" + data.getMeasurementSetup().getNegativeFastPolarityReversalTime() +
                "\nВремя положительных импульсов ПП :\n\t" + data.getMeasurementSetup().getPositiveFastPolarityReversalTime() +
                "\nАмплитуда положительного импульса измерения :\n\t" + data.getMeasurementSetup().getNegativeAmplitudeMeasurePulses() +
                "\nАмплитуда отрицательного импульса измерения :\n\t" + data.getMeasurementSetup().getPositiveAmplitudeMeasurePulses() +
                "\nВремя отрицательного импульса измерения :\n\t" + data.getMeasurementSetup().getNegativeMeasureTime() +
                "\nВремя положительного импульса измерения :\n\t" + data.getMeasurementSetup().getPositiveMeasureTime() +
                ""
        );
    }
}
