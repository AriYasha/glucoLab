package com.validation;

import com.comPort.Control;
import com.entity.Data;
import com.entity.MeasurementSetup;
import com.file.Write;
import com.graph.MultipleAxesLineChart;
import com.sample.Controller;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataFromComPortValidation {

    final static Logger logger = Logger.getLogger(Controller.class);

    private Controller controller;
    private Control control;
    private MeasurementSetup setup;
    private long millisStart;
    private XYChart.Series<Number, Number> series;
    private XYChart.Series<Number, Number> currentSeries;
    private boolean isPolarityChanged = false;
    private Data currentData;

    public DataFromComPortValidation(Controller controller, Control control) {
        this.controller = controller;
        this.control = control;
        currentData = new Data();
    }

    public void checkCmd(List<Byte> command, MeasurementSetup setup) throws IOException {
        this.setup = setup;
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
            byte[] bytes = {command.get(2), command.get(1)};
            int data = control.getIntFromArray(bytes);
            if (isPolarityChanged) {
                addToSeries(-data, -setup.getNegativeAmplitudeMeasurePulses());
            } else {
                addToSeries(data, setup.getPositiveAmplitudeMeasurePulses());
            }

        } else if (isSetup(command)) {
            System.out.println("SETUP_CMD");
            parseSetupCMD(command);
        }

    }

    private boolean isSetup(List<Byte> command) {
        return command.get(0).equals(Control.SETUP_CMD) &&
                command.get(6).equals(Control.END_CMD) &&
                command.get(18).equals(Control.END_CMD) &&
                command.get(29).equals(Control.SETUP_CMD);
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
                Platform.runLater(() -> {
                    controller.measureStatLabel.setText("Вставьте полоску");
                });
                break;
            case Control.STRIP_INSERTED_STAT:
                System.out.println("STRIP_INSERTED_STAT");
                break;
            case Control.DROP_WAITING_STAT:
                System.out.println("DROP_WAITING_STAT");
                Platform.runLater(() -> {
                    controller.measureStatLabel.setText("Ожидание капли");
                });
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
                endMeasure();
                break;

        }

    }

    private void endMeasure() {
        currentData.setMeasurementSetup(setup);
        Platform.runLater(() -> {
            controller.measureStatLabel.setText("Измерение завершено");
//            XYChart.Series series = (XYChart.Series) controller.glucoChart.getData().get(0);
//            ObservableList<XYChart.Data> dataFromPlot = series.getData();
//            currentData.setCurrentMeasurement(dataFromPlot);
//            Write.writeNewData(currentData);
//            for (XYChart.Data newData: dataFromPlot) {
//                logger.debug("x = " + newData.getXValue());
//                logger.debug("y = " + newData.getYValue());
//            }
        });
        XYChart.Series series = (XYChart.Series) controller.glucoChart.getData().get(0);
        ObservableList<XYChart.Data> dataFromPlot =  series.getData();
        ArrayList<Number> xValues = new ArrayList<>();
        ArrayList<Number> yValues = new ArrayList<>();
        for (XYChart.Data newData: dataFromPlot) {
            xValues.add((Number) newData.getXValue());
            yValues.add((Number) newData.getYValue());
//            logger.debug("x = " + newData.getXValue());
//            logger.debug("y = " + newData.getYValue());
        }
        logger.debug("dataFromPlot");
        logger.debug(dataFromPlot);
        currentData.setCurrentXMeasurement(xValues);
        currentData.setCurrentYMeasurement(yValues);
        logger.debug(currentData.toString());
        Write.writeNewData(currentData);

    }

    private void startMeasure() {
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
            controller.measureStatLabel.setText("Измерение . . .");
        });
        millisStart = System.currentTimeMillis();
    }

    private void addToSeries(int data, int voltage) {
        Platform.runLater(() -> {
            series.getData().add(new XYChart.Data<>(System.currentTimeMillis() - millisStart, data));
            currentSeries.getData().add(new XYChart.Data<>(System.currentTimeMillis() - millisStart, voltage));

        });
    }

    private void parseSetupCMD(List<Byte> command) {
        byte[] bytes = {command.get(3), command.get(2)};
        setup.setLeakingTime(control.getIntFromArray(bytes));
        controller.waitingTimeEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        bytes[0] = command.get(5);
        bytes[1] = command.get(4);
        setup.setPauseTime(control.getIntFromArray(bytes));
        controller.pauseTimeEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        bytes[0] = command.get(8);
        bytes[1] = command.get(7);
        setup.setPositiveFastPolarityReversalTime(control.getIntFromArray(bytes));
        controller.positiveTimeFastWavesEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        bytes[0] = command.get(10);
        bytes[1] = command.get(9);
        setup.setNegativeFastPolarityReversalTime(control.getIntFromArray(bytes));
        controller.negativeTimeFastWavesEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        setup.setFirstPolarityReversal(command.get(11) != 0);
        if (command.get(11) != 0) {
            controller.positiveFastHalfWaveRadioB.setSelected(true);
        } else {
            controller.negativeFastHalfWaveRadioB.setSelected(true);
        }
        bytes[0] = command.get(13);
        bytes[1] = command.get(12);
        setup.setPositiveAmplitudeFastPolarityPulses(control.getIntFromArray(bytes));
        controller.positiveAmpFastWavesEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        bytes[0] = command.get(15);
        bytes[1] = command.get(14);
        setup.setNegativeAmplitudeFastPolarityPulses(control.getIntFromArray(bytes));
        controller.negativeAmpFastWavesEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        bytes[0] = command.get(17);
        bytes[1] = command.get(16);
        setup.setQuantityFastPolarityPulses(control.getIntFromArray(bytes));
        controller.quantityFastPulsesEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        bytes[0] = command.get(20);
        bytes[1] = command.get(19);
        setup.setPositiveMeasureTime(control.getIntFromArray(bytes));
        controller.positiveTimeMeasureEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        bytes[0] = command.get(22);
        bytes[1] = command.get(21);
        setup.setNegativeMeasureTime(control.getIntFromArray(bytes));
        controller.negativeTimeMeasureEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        setup.setFirstPolarityMeasure(command.get(23) != 0);
        if (command.get(23) != 0) {
            controller.positiveMeasureRadioB.setSelected(true);
        } else {
            controller.negativeMeasureRadioB.setSelected(true);
        }
        bytes[0] = command.get(25);
        bytes[1] = command.get(24);
        setup.setPositiveAmplitudeMeasurePulses(control.getIntFromArray(bytes));
        controller.positiveAmpMeasureEdit.setText(String.valueOf(control.getIntFromArray(bytes)));
        bytes[0] = command.get(27);
        bytes[1] = command.get(26);
        setup.setNegativeAmplitudeMeasurePulses(control.getIntFromArray(bytes));
        controller.negativeAmpMeasureEdit.setText(String.valueOf(control.getIntFromArray(bytes)));

    }

    private void polarityChange() {
        isPolarityChanged = true;
    }

    private void errorChecker(byte command) {

    }

    private void stripChecker(byte command) {
        currentData.setStripType(command);
        switch (command) {
            case Control.FIRST_STRIP_TYPE:
                System.out.println("FIRST_STRIP_TYPE");
                Platform.runLater(() -> {
                    Image stripType = new Image("images/strip1.jpg", true);
                    controller.stripTypeImage.setImage(stripType);
                    controller.stripTypeLabel.setVisible(true);
                    controller.stripTypeLabel.setText("Полоска № 1");
                });
                break;
            case Control.SECOND_STRIP_TYPE:
                System.out.println("SECOND_STRIP_TYPE");
                break;
            case Control.THIRD_STRIP_TYPE:
                System.out.println("THIRD_STRIP_TYPE");
                break;
            case Control.FOURTH_STRIP_TYPE:
                System.out.println("FOURTH_STRIP_TYPE");
                Platform.runLater(() -> {
                    Image stripType = new Image("images/strip4.jpg", true);
                    controller.stripTypeImage.setImage(stripType);
                    controller.stripTypeLabel.setVisible(true);
                    controller.stripTypeLabel.setText("Полоска № 4");
                });
                break;
            case Control.ZERO_STRIP_TYPE:
                System.out.println("ZERO_STRIP_TYPE");
                Platform.runLater(() -> {
                    Image stripType = new Image("images/strip0.jpg", true);
                    controller.stripTypeImage.setImage(stripType);
                    controller.stripTypeLabel.setVisible(true);
                    controller.stripTypeLabel.setText("Полоска № 0");
                });
                break;

        }

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
