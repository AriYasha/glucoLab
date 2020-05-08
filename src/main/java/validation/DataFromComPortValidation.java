package validation;

import comPort.Control;
import graph.MultipleAxesLineChart;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import sample.Controller;

import java.io.IOException;
import java.util.List;

public class DataFromComPortValidation {

    private Controller controller;
    private Control control;
    private long millisStart;
    private XYChart.Series<Number, Number> series;
    private XYChart.Series<Number, Number> currentSeries;
    private boolean isPolarityChanged = false;

    public DataFromComPortValidation(Controller controller, Control control) {
        this.controller = controller;
        this.control = control;
    }

    public void checkCmd(List<Byte> command) throws IOException {
        if (isCommand(command)) {
//            Platform.runLater(() -> controller.connectionLabel.setText("bye"));
            switch (command.get(1)) {
                case Control.STAT_CMD:
                    statusChecker(command.get(2));
                    break;
                case Control.ERR_CMD:
                    errorChecker(command.get(2));
                    break;
                case Control.STRIP_NUMBER_CMD:
                    stripChecker(command.get(2));
                    break;
            }
        } else if (isData(command)) {
            byte[] bytes = {command.get(1),command.get(2)};
            int data = control.getIntFromArray(bytes);
            //System.out.println(data);
            if(isPolarityChanged){
                addToSeries(-data, -300);
            } else {
                addToSeries(data, 300);
            }

        }

    }

    private void statusChecker(byte command) {
        switch (command) {
            case Control.NOT_CONNECTED_STAT:
                System.out.println("NOT CONNECTED");
                break;
            case Control.CONNECTED_STAT:
                System.out.println("CONNECTED");
                break;
            case Control.STRIP_WAITING_STAT:
                System.out.println("STRIP_WAITING");
                break;
            case Control.STRIP_INSERTED_STAT:
                System.out.println("STRIP_INSERTED_STAT");
                break;
            case Control.DROP_WAITING_STAT:
                System.out.println("DROP_WAITING_STAT");
                break;
            case Control.DROP_DETECTED_STAT:
                System.out.println("DROP_DETECTED_STAT");
                break;
            case Control.LEAK_WAITING_STAT:
                System.out.println("LEAK_WAITING_STAT");
                break;
            case Control.LEAKING_STAT:
                System.out.println("LEAKING_STAT");
                break;
            case Control.FAST_POLARITY_BEGIN_STAT:
                System.out.println("FAST_POLARITY_BEGIN_STAT");
                break;
            case Control.FAST_POLARITY_END_STAT:
                System.out.println("FAST_POLARITY_END_STAT");
                break;
            case Control.START_MEASURE_STAT:
                System.out.println("START_MEASURE_STAT");
                startMeasure();
                break;
            case Control.POLARITY_CHANGED_STAT:
                System.out.println("POLARITY_CHANGED_STAT");
                polarityChange();
                break;
            case Control.END_MEASURE_STAT:
                System.out.println("END_MEASURE_STAT");
                break;

        }

    }

    private void startMeasure(){
        Platform.runLater(() -> controller.tabPane.getSelectionModel().select(1));
        series = new XYChart.Series<>();
        currentSeries = new XYChart.Series<>();
        series.setName("Ток");
        currentSeries.setName("Напряжение");
        Platform.runLater(() -> controller.glucoChart.setAnimated(false));
        Platform.runLater(() -> controller.glucoChart.getData().add(series));
        Platform.runLater(() -> {
            MultipleAxesLineChart voltageChart = new MultipleAxesLineChart(controller.glucoChart, controller.graphStackPane);
            voltageChart.addSeries(currentSeries, Color.RED);
            controller.legendPane.getChildren().add(voltageChart.getLegend());
        });
        millisStart = System.currentTimeMillis();
    }

    private void addToSeries(int data, int voltage){
        Platform.runLater(() -> {
            series.getData().add(new XYChart.Data<>(System.currentTimeMillis() - millisStart, data));
            currentSeries.getData().add(new XYChart.Data<>(System.currentTimeMillis() - millisStart, voltage));

        });
    }

    private void polarityChange(){
        isPolarityChanged = true;
    }

    private void errorChecker(byte command) {

    }

    private void stripChecker(byte command) {

    }

    private boolean isCommand(List<Byte> command) {
        return command.get(0).equals(Control.START_CMD) &&
                command.get(4).equals(Control.END_CMD) &&
                (byte) (command.get(1) + command.get(2)) == command.get(3);
    }

    private boolean isData(List<Byte> command) {
        return command.get(0).equals(Control.END_CMD) &&
                command.get(4).equals(Control.END_CMD) &&
                (byte) (command.get(1) + command.get(2)) == command.get(3);
    }
}
