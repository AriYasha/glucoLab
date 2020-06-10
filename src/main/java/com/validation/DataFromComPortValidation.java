package com.validation;

import com.comPort.Control;
import com.entity.Data;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import com.file.Write;
import com.graph.MultipleAxesLineChart;
import com.controllers.Controller;
import com.graph.MultipleSameAxesLineChart;
import com.sample.CreateStage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;
import org.eclipse.fx.ui.controls.dialog.MessageDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataFromComPortValidation {

    final private static Logger logger = Logger.getLogger(DataFromComPortValidation.class);
    public static boolean isMeasure = false;
    public static boolean isTest = false;

    private Controller controller;
    private Control control;
    private MeasurementSetup setup;
    private PolySetup polySetup;
    private XYChart.Series<Number, Number> series;
    private XYChart.Series<Number, Number> currentSeries;
    private boolean isPolarityChanged = false;
    private Data currentData;

    public DataFromComPortValidation(Controller controller, Control control) {
        this.controller = controller;
        this.control = control;
        currentData = new Data();
    }

    public void checkCmd(List<Byte> command, MeasurementSetup setup, PolySetup polySetup) throws IOException {
        this.setup = setup;
        this.polySetup = polySetup;
        if (isCommand(command)) {
            logger.info("Command detected");
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
                case Control.O_CMD:
                    testChecker();
                    break;
                case Control.NOT_FOUND_CMD:
                    logger.debug("NOT_FOUND_CMD");
                    break;
                case Control.DEVICE_MODE_CMD:
                    logger.debug("DEVICE_MODE_CMD");
                    deviceMode(command.get(2));
                    break;
            }
        } else if (isData(command)) {
            //logger.info("Data detected");
            byte[] bytes = {command.get(2), command.get(1)};
            int time = control.getIntFromArray(bytes);
            bytes[0] = command.get(6);
            bytes[1] = command.get(5);
            int dataInt = control.getIntFromArray(bytes);
            bytes[0] = command.get(4);
            bytes[1] = command.get(3);
            int voltage = control.getIntFromArray(bytes);
            float data = ((float) dataInt) / 100;
            if (setup.isFirstPolarityMeasure()) {
                if (isPolarityChanged) {
                    addToSeries(-data, -voltage, time);
                } else {
                    addToSeries(data, voltage, time);
                }
            } else {
                if (!isPolarityChanged) {
                    addToSeries(-data, -voltage, time);
                } else {
                    addToSeries(data, voltage, time);
                }
            }
        } else if (isSetup(command)) {
            logger.info("Setup detected");
            Platform.runLater(() -> controller.comPortStatus.setText("Настройка устройства принята"));
            parseSetupCMD(command);
        } else if (isPolySetup(command)) {
            logger.info("Setup poly detected");
            Platform.runLater(() -> controller.comPortStatus.setText("Настройка полярограммы устройства принята"));
            parsePolySetupCMD(command);
        } else if (isPolyData(command)) {
//            logger.info("Data poly detected");
            byte[] bytes = {command.get(3), command.get(2)};
            int voltage = control.getIntFromArray(bytes);
            if (command.get(1) == 1) {
                voltage = 0 - voltage;
            }
            bytes[0] = command.get(6);
            bytes[1] = command.get(5);
            int voltageReal = control.getIntFromArray(bytes);
            if (command.get(4) == 1) {
                voltageReal = 0 - voltageReal;
            }

            bytes[0] = command.get(9);
            bytes[1] = command.get(8);
            int current = control.getIntFromArray(bytes);
            if (command.get(7) == 1) {
                current = 0 - current;
            }
            float cur = ((float) current) / 100;
            if (controller.filterCheckBox.isSelected()) {
                addToPolySeries(cur, voltage);
            } else {
                addToPolySeries(cur, voltageReal);
            }
        } else if(isTime(command)){
            logger.info("Real leaking time detected");
            byte[] bytes = {command.get(2), command.get(1)};
            final int realTime = control.getIntFromArray(bytes);
            Platform.runLater(() -> {
                controller.realLeakingTimeEdit.setText(String.valueOf(realTime));
                controller.realLeakingTimeLabel.setText("Время протекания составило : " + String.valueOf(realTime) + " мс");
                controller.realLeakingTimeLabel.setVisible(true);
            });
        }

    }

    private void deviceMode(byte command) {
        switch (command) {
            case Control.MEASURE_MODE:
                logger.debug("MEASURE_MODE");
                Platform.runLater(() -> {
                    controller.mainTabPane.getSelectionModel().select(0);
                    controller.tabPane.getSelectionModel().select(0);
                });
                break;
            case Control.POLY_MODE:
                logger.debug("POLY_MODE");
                Platform.runLater(() -> {
                    controller.mainTabPane.getSelectionModel().select(1);
                    controller.polyTabPane.getSelectionModel().select(0);
                });
                break;
        }
    }

    private void testChecker() {
        isTest = true;
        Platform.runLater(() -> controller.comPortStatus.setText("Тестовая команда принята"));
        logger.debug("TEST_CMD");
    }


    private void statusChecker(byte command) {
        switch (command) {
            case Control.NOT_CONNECTED_STAT:
                logger.info("NOT CONNECTED");
                Platform.runLater(() -> {
                    control.closeConnection();
                    controller.deviceStatus.setText("Произошёл сброс устройства");
                    Image picture = new Image("images/red Ball.png", true);
                    controller.connectImage.setImage(picture);
                    controller.connectionLabel.setText("Не подключено");
                });
                Runnable task = () -> {
                    controller.setConnection();
                };
                Thread thread = new Thread(task);
                thread.start();
                break;
            case Control.CONNECTED_STAT:
                logger.info("CONNECTED");
                Platform.runLater(() -> controller.deviceStatus.setText("Устройство подключено"));
                break;
            case Control.STRIP_WAITING_STAT:
                logger.info("STRIP_WAITING");
                Platform.runLater(() -> {
                    controller.deviceStatus.setText("Ожидание полоски");
                    controller.comPortStatus.setText("");
                    controller.measureStatLabel.setText("Вставьте полоску");
                    Image stripType = new Image("images/stripNoName.jpg", true);
                    controller.stripTypeImage.setImage(stripType);
                    controller.stripTypeLabel.setVisible(false);
                });
                break;
            case Control.STRIP_INSERTED_STAT:
                logger.info("STRIP_INSERTED_STAT");
                Platform.runLater(() -> {
                    controller.deviceStatus.setText("Полоска вставлена");
                    controller.realLeakingTimeLabel.setVisible(false);
//                    controller.glucoChart.getData().clear();
                    controller.polyChart.getData().clear();
                });
                break;
            case Control.DROP_WAITING_STAT:
                logger.info("DROP_WAITING_STAT");
                Platform.runLater(() -> {
                    controller.deviceStatus.setText("Ожидание капли");
                    controller.measureStatLabel.setText("Ожидание капли");
                });
                break;
            case Control.DROP_DETECTED_STAT:
                logger.info("DROP_DETECTED_STAT");
                Platform.runLater(() -> {
                    controller.deviceStatus.setText("Капля обнаружена");
                    controller.measureStatLabel.setText("Капля обнаружена");
                });
                break;
            case Control.LEAK_WAITING_STAT:
                logger.info("LEAK_WAITING_STAT");
                Platform.runLater(() -> {
                    controller.deviceStatus.setText("Ожидание протекания");
                    controller.measureStatLabel.setText("Ожидание протекания");
                });
                break;
            case Control.LEAKING_STAT:
                logger.info("LEAKING_STAT");
                Platform.runLater(() -> {
                    controller.deviceStatus.setText("Есть протекание");
                    controller.measureStatLabel.setText("Есть протекание");
                });
                break;
            case Control.FAST_POLARITY_BEGIN_STAT:
                logger.info("FAST_POLARITY_BEGIN_STAT");
                Platform.runLater(() -> {
                    controller.deviceStatus.setText("Быстрая ПП начата");
                    controller.measureStatLabel.setText("Быстрая ПП начата");
                });
                break;
            case Control.FAST_POLARITY_END_STAT:
                logger.info("FAST_POLARITY_END_STAT");
                Platform.runLater(() -> {
                    controller.deviceStatus.setText("Быстрая ПП окончена");
                    controller.measureStatLabel.setText("Быстрая ПП окончена");
                });
                break;
            case Control.START_MEASURE_STAT:
                isMeasure = true;
                logger.info("START_MEASURE_STAT");
                Platform.runLater(() -> {
                    controller.deviceStatus.setText("Начато измерение");
                    controller.measureStatLabel.setText("Начато измерение");
                });
                startMeasure();
                break;
            case Control.POLARITY_CHANGED_STAT:
                logger.info("POLARITY_CHANGED_STAT");
                polarityChange();
                break;
            case Control.END_MEASURE_STAT:
                logger.info("END_MEASURE_STAT");
                Platform.runLater(() -> controller.deviceStatus.setText("Измерение окончено"));
                endMeasure();
                isPolarityChanged = false;
                isMeasure = false;
                break;
            case Control.START_POLY_STAT:
                logger.info("START_POLY_STAT");
                Platform.runLater(() -> controller.deviceStatus.setText("Построение полярограммы . . ."));
                startPolyMeasure();
                break;
            case Control.END_POLY_STAT:
                logger.info("END_POLY_STAT");
                Platform.runLater(() -> controller.deviceStatus.setText("Построение полярограммы окончено"));
                endPolyMeasure();
                break;

        }

    }

    private void endMeasure() {
        try {
            //currentData.setMeasurementSetup(setup);
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
            ObservableList<XYChart.Data> dataFromPlot = series.getData();
            ArrayList<Number> xValues = new ArrayList<>();
            ArrayList<Number> yValues = new ArrayList<>();
            for (XYChart.Data newData : dataFromPlot) {
                xValues.add((Number) newData.getXValue());
                yValues.add((Number) newData.getYValue());
//            logger.debug("x = " + newData.getXValue());
//            logger.debug("y = " + newData.getYValue());
            }
            logger.debug("dataFromPlot");
            logger.debug(dataFromPlot);
            currentData.setCurrentXMeasurement(xValues);
            currentData.setCurrentYMeasurement(yValues);
            setup.setData(currentData);
            Platform.runLater(() -> {
                String fileName = Write.generateFileName(setup);
                CreateStage dialog = new CreateStage(fileName);
                fileName = dialog.getFileName();
                if (!fileName.contains(".gl")) {
                    fileName = fileName.concat(".gl");
                }
                String comment = dialog.getComment();
                if (!comment.isEmpty()) {
                    currentData.setComment(comment);
                    setup.setData(currentData);
                }
                if (dialog.isPressed()) {
                    Write.writing(setup, fileName);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startMeasure() {
        Platform.runLater(() -> {
            controller.mainTabPane.getSelectionModel().select(0);
            controller.tabPane.getSelectionModel().select(1);
        });
        series = new XYChart.Series<>();
        currentSeries = new XYChart.Series<>();
        series.setName("Ток");
        currentSeries.setName("Напряжение");
        Platform.runLater(() -> {
            controller.glucoChart.setAnimated(false);
            controller.glucoChart.getData().add(series);
            MultipleAxesLineChart voltageChart = new MultipleAxesLineChart(controller.glucoChart, controller.graphStackPane);
            voltageChart.addSeries(currentSeries, Color.RED);
            controller.legendPane.getChildren().add(voltageChart.getLegend());
            controller.measureStatLabel.setText("Измерение . . .");
        });
    }

    private void startPolyMeasure() {
        Platform.runLater(() -> {
            controller.mainTabPane.getSelectionModel().select(1);
            controller.polyTabPane.getSelectionModel().select(1);
        });
        series = new XYChart.Series<>();
        currentSeries = new XYChart.Series<>();
        series.setName("Полярограмма");
        Platform.runLater(() -> {
            controller.polyChart.setAnimated(false);
            controller.polyChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
            controller.polyChart.getData().add(series);
            controller.measureStatLabel.setText("Построение полярограммы . . .");
        });
    }


    private void endPolyMeasure() {
        Platform.runLater(() -> {
            controller.measureStatLabel.setText("Полярограмма завершена");
        });
        try {
            XYChart.Series series = (XYChart.Series) controller.polyChart.getData().get(0);
            ObservableList<XYChart.Data> dataFromPlot = series.getData();
            ArrayList<Number> xValues = new ArrayList<>();
            ArrayList<Number> yValues = new ArrayList<>();
            for (XYChart.Data newData : dataFromPlot) {
                xValues.add((Number) newData.getXValue());
                yValues.add((Number) newData.getYValue());
            }
            logger.debug("dataFromPlot");
            logger.debug(dataFromPlot);
            currentData.setCurrentXMeasurement(xValues);
            currentData.setCurrentYMeasurement(yValues);
            polySetup.setData(currentData);
            Platform.runLater(() -> {
                String fileName = Write.generateFileName(polySetup);
                CreateStage dialog = new CreateStage(fileName);
                fileName = dialog.getFileName();
                if (!fileName.contains(".pl")) {
                    fileName = fileName.concat(".pl");
                }
                String comment = dialog.getComment();
                if (!comment.isEmpty()) {
                    currentData.setComment(comment);
                    polySetup.setData(currentData);

                } else {
                    currentData.setComment("Комментарии отсутствуют");
                    polySetup.setData(currentData);
                }
                if (dialog.isPressed()) {
                    Write.writing(polySetup, fileName);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void addToSeries(float data, int voltage, int time) {
        Platform.runLater(() -> {
            series.getData().add(new XYChart.Data<>(time, data));
            currentSeries.getData().add(new XYChart.Data<>(time, voltage));

        });
    }

    private void addToPolySeries(float current, int voltage) {
        Platform.runLater(() -> {
            series.getData().add(new XYChart.Data<>(voltage, current));
            //series.getData().ad
            //controller.polyChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        });
    }

    private void parseSetupCMD(List<Byte> command) {
        Platform.runLater(() -> {
            controller.mainTabPane.getSelectionModel().select(0);
            controller.tabPane.getSelectionModel().select(0);
        });
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
        successfulReceive();
        logger.debug("end setup");

    }

    private void parsePolySetupCMD(List<Byte> command) {
        Platform.runLater(() -> {
            controller.mainTabPane.getSelectionModel().select(1);
            controller.polyTabPane.getSelectionModel().select(0);
        });
        logger.debug(command);
        byte[] bytes = {command.get(3), command.get(2)};
        int digit = control.getIntFromArray(bytes);
        if (command.get(1) == 1) {
            digit = 0 - digit;
        }
        polySetup.setBeginPoint(digit);
        final int value = digit;

        bytes[0] = command.get(6);
        bytes[1] = command.get(5);
        digit = control.getIntFromArray(bytes);
        if (command.get(4) == 1) {
            digit = 0 - digit;
        }
        polySetup.setMediumPoint(digit);
        final int value1 = digit;
        bytes[0] = command.get(9);
        bytes[1] = command.get(8);
        digit = control.getIntFromArray(bytes);
        if (command.get(7) == 1) {
            digit = 0 - digit;
        }
        polySetup.setLastPoint(digit);
        final int value2 = digit;
        byte[] timeBytes = {command.get(13), command.get(12), command.get(11)};
        digit = control.getIntFromArray(timeBytes);
        polySetup.setIncreaseTime(digit);
        final int value3 = digit;
        timeBytes[0] = command.get(16);
        timeBytes[1] = command.get(15);
        timeBytes[2] = command.get(14);
        digit = control.getIntFromArray(timeBytes);
        polySetup.setDecreaseTime(digit);
        final int value4 = digit;
        bytes[0] = command.get(19);
        bytes[1] = command.get(18);
        digit = control.getIntFromArray(bytes);
        polySetup.setQuantityReapeted(digit);
        final int value5 = digit;
        bytes[0] = command.get(21);
        bytes[1] = command.get(20);
        digit = control.getIntFromArray(bytes);
        polySetup.setDiscrDACmV(digit);
        bytes[0] = command.get(23);
        bytes[1] = command.get(22);
        digit = control.getIntFromArray(bytes);
        polySetup.setNullDiscrDAC(digit);
        Platform.runLater(() -> {
            controller.beginPointEdit.setText(String.valueOf(value));
            controller.mediumPointEdit.setText(String.valueOf(value1));
            controller.lastPointEdit.setText(String.valueOf(value2));
            controller.increaseTimeEdit.setText(String.valueOf(value3));
            controller.decreaseTimeEdit.setText(String.valueOf(value4));
            controller.quantityReapetedEdit.setText(String.valueOf(value5));
        });
        successfulReceive();
        logger.debug("end poly setup");

    }

    private void polarityChange() {
        isPolarityChanged = true;
    }

    private void errorChecker(byte command) {
        Platform.runLater(() -> {
            controller.comPortStatus.setText("Ошибка устройства");
            controller.measureStatLabel.setText("Произошла ошибка");
        });
        switch (command) {
            case Control.STRIP_TYPE_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("Ошибка определения типа полоски"));
                break;
            case Control.VOLTAGE_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("Не удалось установить заданное напряжение"));
                break;
            case Control.LEAKING_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("Не произошло протекание"));
                break;
            case Control.E80_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E80_ERROR"));
                break;
            case Control.E81_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E81_ERROR"));
                break;
            case Control.E82_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E82_ERROR"));
                break;
            case Control.E83_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E83_ERROR"));
                break;
            case Control.E84_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E84_ERROR"));
                break;
            case Control.E85_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E85_ERROR"));
                break;
            case Control.E86_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E86_ERROR"));
                break;
            case Control.E87_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E87_ERROR"));
                break;
            case Control.E88_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E88_ERROR"));
                break;
            case Control.E50_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E50_ERROR"));
                break;
            case Control.E51_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E51_ERROR"));
                break;
            case Control.E52_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E52_ERROR"));
                break;
            case Control.E53_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E53_ERROR"));
                break;
            case Control.E54_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E54_ERROR"));
                break;
            case Control.E55_ERROR:
                Platform.runLater(() -> controller.comPortStatus.setText("E55_ERROR"));
                break;


        }

    }

    private void stripChecker(byte command) {
        currentData.setStripType(command);
        switch (command) {
            case Control.FIRST_STRIP_TYPE:
                logger.info("FIRST_STRIP_TYPE");
                Platform.runLater(() -> {
                    Image stripType = new Image("images/strip1.jpg", true);
                    controller.stripTypeImage.setImage(stripType);
                    controller.stripTypeLabel.setVisible(true);
                    controller.stripTypeLabel.setText("Полоска № 1");
                });
                break;
            case Control.SECOND_STRIP_TYPE:
                logger.info("SECOND_STRIP_TYPE");
                break;
            case Control.THIRD_STRIP_TYPE:
                logger.info("THIRD_STRIP_TYPE");
                break;
            case Control.FOURTH_STRIP_TYPE:
                logger.info("FOURTH_STRIP_TYPE");
                Platform.runLater(() -> {
                    Image stripType = new Image("images/strip4.jpg", true);
                    controller.stripTypeImage.setImage(stripType);
                    controller.stripTypeLabel.setVisible(true);
                    controller.stripTypeLabel.setText("Полоска № 4");
                });
                break;
            case Control.ZERO_STRIP_TYPE:
                logger.info("ZERO_STRIP_TYPE");
                Platform.runLater(() -> {
                    Image stripType = new Image("images/strip0.jpg", true);
                    controller.stripTypeImage.setImage(stripType);
                    controller.stripTypeLabel.setVisible(true);
                    controller.stripTypeLabel.setText("Полоска № 0");
                });
                break;
        }
    }

    private void successfulReceive(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успех");
            alert.setHeaderText("Настройка принята");
            alert.setContentText("");
            alert.show();
        });
    }

    private boolean isCommand(List<Byte> command) {
        return command.get(0).equals(Control.START_CMD) &&
                command.get(4).equals(Control.END_CMD) &&
                (byte) (command.get(1) + command.get(2)) == command.get(3);
    }

    private boolean isTime(List<Byte> command) {
        return command.get(0).equals(Control.TIME_CMD) &&
                command.get(4).equals(Control.TIME_CMD) &&
                (byte) (command.get(1) + command.get(2)) == command.get(3);
    }

    private boolean isData(List<Byte> command) {
        return command.get(0).equals(Control.END_CMD) &&
                command.get(8).equals(Control.END_CMD) /*&&
                (byte) (command.get(1) + command.get(2)) == command.get(3)*/;
    }

    private boolean isPolyData(List<Byte> command) {
        return command.get(0).equals(Control.POLY_DATA_CMD) &&
                command.get(11).equals(Control.POLY_DATA_CMD) /*&&
                (byte) (command.get(1) + command.get(2)) == command.get(3)*/;
    }

    private boolean isPolySetup(List<Byte> command) {
        return command.get(0).equals(Control.POLY_SETUP_CMD) &&
                command.get(10).equals(Control.END_CMD) &&
                command.get(17).equals(Control.END_CMD) &&
                command.get(25).equals(Control.POLY_SETUP_CMD);
    }

    private boolean isSetup(List<Byte> command) {
        return command.get(0).equals(Control.SETUP_CMD) &&
                command.get(6).equals(Control.END_CMD) &&
                command.get(18).equals(Control.END_CMD) &&
                command.get(29).equals(Control.SETUP_CMD);
    }
}
