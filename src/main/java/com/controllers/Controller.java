package com.controllers;

import com.comPort.ComPortConnection;
import com.comPort.Control;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import com.exception.ComPortException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.file.ChooseFile;
import com.file.Read;
import com.file.Write;
import com.graph.VisualisationPlot;
import com.jfoenix.controls.JFXTabPane;
import com.validation.DataFromComPortValidation;
import com.validation.UIValidation;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class Controller implements Initializable {

    final static Logger logger = Logger.getLogger(Controller.class);

    public Tab setupTab;
    public LineChart glucoChart;
    public AnchorPane graphPane;
    public NumberAxis glocoTimeAxis;
    public NumberAxis glucoAmpAxis;
    public StackPane graphStackPane;
    public AnchorPane legendPane;
    public MeasurementSetup setup;
    public PolySetup polySetup;
    public Label measureStatLabel;
    public ImageView stripTypeImage;
    public Label stripTypeLabel;
    public Label waitingErrorLabel;
    public Label pauseErrorLabel;
    public Label positiveTimeFastErrorLabel;
    public Label negativeTimeFastErrorLabel;
    public Label positiveTimeMeasureErrorLabel;
    public Label negativeTimeMeasureErrorLabel;
    public Label positiveAmpFastErrorLabel;
    public Label negativeAmpFastErrorLabel;
    public Label positiveAmpMeasureErrorLabel;
    public Label negativeAmpMeasureErrorLabel;
    public Label quantityErorLabel;
    public MenuBar menuBar;
    public Label deviceStatus;
    public Tab measureTab;
    public Tab polyTab;
    public ImageView sendImage;
    public Button sendPolyButton;
    public LineChart<Number, Number> visualPolyPlot;
    public Tab setupPolyTab;
    public Tab graphPolyTab;
    public StackPane graphPolyStackPane;
    public LineChart polyChart;
    public AnchorPane legendPolyPane;
    public JFXTabPane mainTabPane;
    public NumberAxis xTimeVisualPoly;
    public NumberAxis yAmpVisualPoly;
    public ImageView sendPolyImage;
    public TextField mediumPointEdit;
    public TextField increaseTimeEdit;
    public TextField decreaseTimeEdit;
    public TextField lastPointEdit;
    public TextField quantityReapetedEdit;
    public TextField beginPointEdit;
    public Label mediumPointError;
    public Label beginPointError;
    public Label increaseTimeError;
    public Label decreaseTimeError;
    public Label quantityReapetedError;
    public Label lastPointError;
    public JFXTabPane polyTabPane;
    public Label comPortStatus;
    public CheckBox filterCheckBox;
    public TextField realLeakingTimeEdit;
    public Label realLeakingTimeLabel;
    public Button chartClearButton;
    public TextField realLeakingQuantityEdit;
    public TextField realLeakingCurrentEdit;
    public Label lastValuesLabel;
    private ComPortConnection comPortConnection;
    private Control control;
    private UIValidation uiValidation;
    private XYChart.Series<Number, Number> currentSeries;

    public Label coordinateLabel;
    public Button updatePortsButton;
    public ToggleButton openPortButton;
    public ChoiceBox<String> portChoiceBox;
    public ChoiceBox speedChoiceBox;
    public ChoiceBox parityChoiceBox;
    public ChoiceBox stopBitsChoiceBox;
    public ChoiceBox sizeCharChoiceBox;
    public CheckBox defaultPortCheckBox;
    public Button sendDataButton;
    public ImageView connectImage;
    public ToggleGroup choiceBigWave;
    public RadioButton positiveFastHalfWave;
    public ToggleGroup choiceLittleWave;
    public RadioButton negativeFastHalfWave;
    public TextField pauseTimeEdit;
    public TextField positiveTimeFastWavesEdit;
    public TextField negativeTimeFastWavesEdit;
    public Label connectionLabel;
    public Tab graphTab;
    public TextField waitingTimeEdit;
    public CheckBox pauseTimeCheckBox;
    public CheckBox waitingTimeCheckBox;
    public TextField quantityFastPulsesEdit;
    public TextField commonFastPulsesTimeEdit;
    public RadioButton positiveFastHalfWaveRadioB;
    public TextField negativeTimeMeasureEdit;
    public RadioButton negativeFastHalfWaveRadioB;
    public TextField positiveAmpMeasureEdit;
    public TextField positiveTimeMeasureEdit;
    public TextField negativeAmpMeasureEdit;
    public RadioButton positiveMeasureRadioB;
    public RadioButton negativeMeasureRadioB;
    public TextField commonMeasureTimeEdit;
    public Label waitTimeLabel;
    public Label pauseTimeLabel;
    public LineChart<Number, Number> visualPlot;
    public NumberAxis xTimeVisual;
    public NumberAxis yAmpVisual;
    public TextField negativeAmpFastWavesEdit;
    public TextField positiveAmpFastWavesEdit;
    public TabPane tabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filterCheckBox.setVisible(false);
        realLeakingTimeLabel.setVisible(false);
        uiValidation = new UIValidation(this);
        uiValidation.hideErrorLabels();
        uiValidation.setImages();
        menuBarSetup();
        setTooltips();
        uiValidation.connectionSetup();
        setDisableElements();
        setup = new MeasurementSetup();
        polySetup = new PolySetup();
        polySetup = uiValidation.renderPolyVisualisation();
        renderVisualisation();
        coordinateLabel.setVisible(false);

        uiValidation.visualisationSetup(visualPlot, xTimeVisual, yAmpVisual);
        uiValidation.visualisationSetup(visualPolyPlot, xTimeVisualPoly, yAmpVisualPoly);

        setPlotTooltip();
        Runnable task = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setConnection();
            //connectionRequest();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public Control getControl() {
        return control;
    }

    private void setPlotTooltip() {
        ObservableList<XYChart.Data> dataList = ((XYChart.Series) visualPlot.getData().get(0)).getData();
        dataList.forEach(data -> {
            Node node = data.getNode();
            Tooltip tooltip = new Tooltip("Время = " + data.getXValue().toString() + ", мс\nАмплитуда = " + data.getYValue().toString() + ", мВ");
            Tooltip.install(node, tooltip);
        });
    }

    public void setConnection() {
        boolean isConnected = false;
        List portList = new ArrayList();
        portList.addAll(portChoiceBox.getItems());
        byte[] readBytes;
        while (!isConnected) {
            for (Object port : portList) {
                try {
                    comPortConnection = ComPortConnection.getInstance((String) port);
                    comPortConnection.openPort();
                    control = new Control(comPortConnection, this, setup, polySetup);
                    control.sendTest();
                    int timeOut = 0;
                    while (control.bytesAvailable() <= 5) {
                        timeOut++;
                        if (timeOut > 165535) break;
                    }
                    readBytes = control.readBytes();
                    logger.debug(Arrays.toString(readBytes));
                    if (Arrays.equals(readBytes, Control.CONTROL_ARRAY)) {
                        control.addListener();
                        control.serialEvent(new SerialPortEvent(control.getUserPort(), SerialPort.LISTENING_EVENT_DATA_AVAILABLE));
                        Image picture = new Image("images/green Ball.png", true);
                        connectImage.setImage(picture);
                        Platform.runLater(() -> connectionLabel.setText("Подключено"));
                        Platform.runLater(() -> deviceStatus.setText("Ожидание полоски"));
                        Platform.runLater(() -> openPortButton.setText("Прервать соединение"));
                        openPortButton.setSelected(true);
                        //control.addListener();
                        isConnected = true;
                        break;
                    } else {
                        comPortConnection.close();
                    }

                } catch (ComPortException e) {
                    Platform.runLater(() -> connectionLabel.setText("Не удалось подключиться автоматически"));
                    logger.error(e);
                    logger.error(e.getMessage());
                } catch (IOException e) {
                    logger.error(e);
                    logger.error(e.getMessage());
                }
            }
            portList.clear();
            portList.addAll(Arrays.asList(ComPortConnection.getPortNames()));
            logger.debug(portList);
        }
        logger.debug("end trying to connect");

    }

    private void connectionRequest() {
        try {
            Thread.sleep(3000);
            while (true) {
                if (comPortConnection.isBusy()) {
                    if (!DataFromComPortValidation.isMeasure) {
                        Thread.sleep(13000);
                        control.sendTest();
                        Thread.sleep(100);
                        for (int i = 0; i < 3; i++) {
                            if (DataFromComPortValidation.isTest) {
                                DataFromComPortValidation.isTest = false;
                                break;
                            } else {
                                control.sendTest();
                                Thread.sleep(300);
                            }
                            if (i == 2) {
                                uiValidation.setOnDisconnected();
                                comPortConnection.closePort();
                            }
                        }
                    }
                } else {
                    setConnection();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void openPort(ActionEvent actionEvent) {
        if (defaultPortCheckBox.isSelected() && openPortButton.isSelected()) {
            try {
                comPortConnection = ComPortConnection.getInstance(ComPortConnection.getPortName());
                comPortConnection.openPort();
                control = new Control(comPortConnection, this, setup, polySetup);
                control.addListener();
                Image picture = new Image("images/green Ball.png", true);
                connectImage.setImage(picture);
                connectionLabel.setText("Подключено");
                deviceStatus.setText("Ожидание полоски");
                openPortButton.setText("Прервать соединение");
            } catch (ComPortException e) {
                sendError(e);
                logger.error(e.getMessage());
            }

        } else if (!defaultPortCheckBox.isSelected() && openPortButton.isSelected()) {
            try {
                int baudRate = Integer.parseInt((String) speedChoiceBox.getValue());
                int size = Integer.parseInt((String) sizeCharChoiceBox.getValue());
                int stopBits = Integer.parseInt((String) stopBitsChoiceBox.getValue());
                int parity;
                switch ((String) parityChoiceBox.getValue()) {
                    case "no":
                        parity = SerialPort.NO_PARITY;
                        break;
                    case "odd":
                        parity = SerialPort.ODD_PARITY;
                        break;
                    case "even":
                        parity = SerialPort.EVEN_PARITY;
                        break;
                    case "mark":
                        parity = SerialPort.MARK_PARITY;
                        break;
                    case "space":
                        parity = SerialPort.SPACE_PARITY;
                        break;
                    default:
                        parity = SerialPort.NO_PARITY;
                        break;
                }
                String portName = portChoiceBox.getValue();
                comPortConnection = ComPortConnection.getInstance(portName);
                comPortConnection.openPort(baudRate, size, stopBits, parity);
                control = new Control(comPortConnection, this, setup, polySetup);
                control.addListener();
                Image picture = new Image("images/green Ball.png", true);
                connectImage.setImage(picture);
                connectionLabel.setText("Подключено");
                deviceStatus.setText("Ожидание полоски");
                openPortButton.setText("Прервать соединение");
            } catch (ComPortException e) {
                sendError(e);
                logger.error(e.getMessage());
            }

        } else if (!openPortButton.isSelected()) {
            try {
                comPortConnection.close();
                openPortButton.setText("Старт соединения");
                Image picture = new Image("images/red Ball.png", true);
                connectImage.setImage(picture);
                connectionLabel.setText("Не подключено");
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void sendError(ComPortException e) {
        openPortButton.setSelected(false);
        Image picture = new Image("images/red Ball.png", true);
        connectImage.setImage(picture);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка подключения");
        alert.setHeaderText("Проверьте подключение устройства");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public void refresh(ActionEvent actionEvent) {
        portChoiceBox.getItems().removeAll(portChoiceBox.getItems());
        String[] portNames = ComPortConnection.getPortNames();
        portChoiceBox.getItems().addAll(portNames);
    }

    public void defaultPortSettings(ActionEvent actionEvent) {
        setDisableElements();
    }

    private void setTooltips() {
        Tooltip waitingTimeTooltip = new Tooltip("Время протекания, Тпр\nЗначение по-умолчанию = 3000 мс");
        waitingTimeEdit.setTooltip(waitingTimeTooltip);
        Tooltip.install(waitingTimeEdit, waitingTimeTooltip);
        Tooltip pauseTimeTooltip = new Tooltip("Время паузы, Тпаузы\nЗначение по-умолчанию = 2000 мс");
        pauseTimeEdit.setTooltip(pauseTimeTooltip);
        Tooltip.install(pauseTimeEdit, pauseTimeTooltip);
        Tooltip positiveTimeFastWavesTooltip = new Tooltip("Время положительного импульса, t+\nЗначение по-умолчанию = 100 мс ");
        positiveTimeFastWavesEdit.setTooltip(positiveTimeFastWavesTooltip);
        Tooltip.install(positiveTimeFastWavesEdit, positiveTimeFastWavesTooltip);
        Tooltip negativeTimeFastWavesTooltip = new Tooltip("Время отрицательного импульса, t-\nЗначение по-умолчанию = 100 мс ");
        negativeTimeFastWavesEdit.setTooltip(negativeTimeFastWavesTooltip);
        Tooltip.install(negativeTimeFastWavesEdit, negativeTimeFastWavesTooltip);
        Tooltip positiveAmpFastWavesTooltip = new Tooltip("Амплитуда положительного импульса, u+\nЗначение по-умолчанию = 300 мВ ");
        positiveAmpFastWavesEdit.setTooltip(positiveAmpFastWavesTooltip);
        Tooltip.install(positiveAmpFastWavesEdit, positiveAmpFastWavesTooltip);
        Tooltip negativeAmpFastWavesTooltip = new Tooltip("Амплитуда отрицательного импульса, u-\nЗначение по-умолчанию = 300 мВ ");
        negativeAmpFastWavesEdit.setTooltip(negativeAmpFastWavesTooltip);
        Tooltip.install(negativeAmpFastWavesEdit, negativeAmpFastWavesTooltip);
        Tooltip positiveTimeMeasureTooltip = new Tooltip("Время положительного импульса, Т+\nЗначение по-умолчанию = 5000 мс ");
        positiveTimeMeasureEdit.setTooltip(positiveTimeMeasureTooltip);
        Tooltip.install(positiveTimeMeasureEdit, positiveTimeMeasureTooltip);
        Tooltip negativeTimeMeasureTooltip = new Tooltip("Время отрицательного импульса, Т-\nЗначение по-умолчанию = 5000 мс ");
        negativeTimeMeasureEdit.setTooltip(negativeTimeMeasureTooltip);
        Tooltip.install(negativeTimeMeasureEdit, negativeTimeMeasureTooltip);
        Tooltip positiveAmpMeasureTooltip = new Tooltip("Амплитуда положительного импульса, U+\nЗначение по-умолчанию = 300 мВ ");
        positiveAmpMeasureEdit.setTooltip(positiveAmpMeasureTooltip);
        Tooltip.install(positiveAmpMeasureEdit, positiveAmpMeasureTooltip);
        Tooltip negativeAmpMeasureTooltip = new Tooltip("Амплитуда отрицательного импульса, U-\nЗначение по-умолчанию = 300 мВ ");
        negativeAmpMeasureEdit.setTooltip(negativeAmpMeasureTooltip);
        Tooltip.install(negativeAmpMeasureEdit, negativeAmpMeasureTooltip);
        Tooltip quantityFastPulsesTooltip = new Tooltip(" Количество импульсов переполюсовки, N\nЗначение по-умолчанию = 5 ");
        quantityFastPulsesEdit.setTooltip(quantityFastPulsesTooltip);
        Tooltip.install(quantityFastPulsesEdit, quantityFastPulsesTooltip);
        Tooltip commonFastPulsesTimeTooltip = new Tooltip("Общяя продолжительность переполюсовки, Tп ");
        commonFastPulsesTimeEdit.setTooltip(commonFastPulsesTimeTooltip);
        Tooltip.install(commonFastPulsesTimeEdit, commonFastPulsesTimeTooltip);
        Tooltip commonMeasureTimeTooltip = new Tooltip("Общяя продолжительность измерения, Tизм ");
        commonMeasureTimeEdit.setTooltip(commonMeasureTimeTooltip);
        Tooltip.install(commonMeasureTimeEdit, commonMeasureTimeTooltip);
    }

    private void setDisableElements() {
        if (defaultPortCheckBox.isSelected()) {
            updatePortsButton.setDisable(true);
            portChoiceBox.setDisable(true);
            speedChoiceBox.setDisable(true);
            parityChoiceBox.setDisable(true);
            stopBitsChoiceBox.setDisable(true);
            sizeCharChoiceBox.setDisable(true);
        } else {
            updatePortsButton.setDisable(false);
            portChoiceBox.setDisable(false);
            speedChoiceBox.setDisable(false);
            parityChoiceBox.setDisable(false);
            stopBitsChoiceBox.setDisable(false);
            sizeCharChoiceBox.setDisable(false);
        }
    }

    public void sendData(ActionEvent actionEvent) {
        /*if (comPortConnection.isBusy()) {
            control.sendByteArray(setup.getTransmitArray());
        }

        tabPane.getSelectionModel().selectNext();
        //glucoChart.getData().add(prepareSeries("Напряжение", (x) -> (double) x * x));

//        MultipleAxesLineChart voltageChart = new MultipleAxesLineChart(glucoChart, graphStackPane);
//        voltageChart.addSeries(prepareSeries("Ток", (x) -> (double) x), Color.GREEN);
//        legendPane.getChildren().add(voltageChart.getLegend());
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        glucoChart.getData().add(series);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        // setup a scheduled executor to periodically put data into the chart
        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // put dummy data onto com.graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // get a random integer between 0-10
            Integer random = ThreadLocalRandom.current().nextInt(10);

            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(new XYChart.Data<>(now.getSeconds(), random));
            });
        }, 0, 1, TimeUnit.SECONDS);
        */
//        byte[] byes = {0x55, (byte) 0x99, 0x00, (byte) 0x99, (byte) 0xAA};
//        control.sendByteArray(byes);
        //renderVisualization();
        if (comPortConnection != null && comPortConnection.isBusy() /*&& control.isTestOk()*/) {
            control.sendByteArray(setup.getTransmitArray());
        } else {
            //uiValidation.setOnDisconnected();
            //comPortConnection.closePort();
        }
//        Runnable task = () -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                logger.error(e.getMessage());
//            }
//            control.sendSetupRequest();
//        };
//        Thread thread = new Thread(task);
//        thread.start();
    }

    private XYChart.Series<Number, Number> prepareSeries(String name, Function<Integer, Double> function) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (int i = 0; i < 360; i++) {
            series.getData().add(new XYChart.Data<>(i, function.apply(i)));
        }
        return series;
    }


    public void disablePauseTime(ActionEvent actionEvent) {
        checkTimeAvailability(pauseTimeCheckBox, pauseTimeEdit, pauseTimeLabel);
    }

    public void disableWaitingTime(ActionEvent actionEvent) {
        checkTimeAvailability(waitingTimeCheckBox, waitingTimeEdit, waitTimeLabel);
    }

    private void checkTimeAvailability(CheckBox checkBox, TextField textField, Label label) {
        if (checkBox.isSelected()) {
            textField.setDisable(true);
            label.setDisable(true);
            textField.setText("0");
        } else {
            textField.clear();
            textField.setDisable(false);
            label.setDisable(false);
        }
    }

    private void renderVisualisation() {
        comPortStatus.setText("");
        String waitingTime = waitingTimeEdit.getText();
        String pauseTime = pauseTimeEdit.getText();
        String negativeTimeFastWaves = negativeTimeFastWavesEdit.getText();
        String positiveTimeFastWaves = positiveTimeFastWavesEdit.getText();
        String quantityFastPulses = quantityFastPulsesEdit.getText();
        String negativeAmpFast = negativeAmpFastWavesEdit.getText();
        String positiveAmpFastWaves = positiveAmpFastWavesEdit.getText();
        String negativeTimeMeasure = negativeTimeMeasureEdit.getText();
        String positiveTimeMeasure = positiveTimeMeasureEdit.getText();
        String negativeAmpMeasure = negativeAmpMeasureEdit.getText();
        String positiveAmpMeasure = positiveAmpMeasureEdit.getText();
        boolean firstPolarityReversal = !negativeFastHalfWaveRadioB.isSelected();
        boolean firstPolarityMeasure = !negativeMeasureRadioB.isSelected();
        if (negativeTimeFastWaves.equals("")) {
            negativeTimeFastWaves = "100";
        }
        if (positiveTimeFastWaves.equals("")) {
            positiveTimeFastWaves = "100";
        }
        if (quantityFastPulses.equals("")) {
            quantityFastPulses = "5";
        }
        if (waitingTime.equals("") && !waitingTimeCheckBox.isSelected()) {
            waitingTime = "3000";
        } else if (waitingTime.equals("") && waitingTimeCheckBox.isSelected()) {
            waitingTime = "0";
        }
        if (pauseTime.equals("") && !pauseTimeCheckBox.isSelected()) {
            pauseTime = "2000";
        } else if (pauseTime.equals("") && pauseTimeCheckBox.isSelected()) {
            pauseTime = "0";
        }
        if (negativeAmpFast.equals("")) {
            negativeAmpFast = "300";
        }
        if (positiveAmpFastWaves.equals("")) {
            positiveAmpFastWaves = "300";
        }
        if (negativeTimeMeasure.equals("")) {
            negativeTimeMeasure = "5000";
        }
        if (positiveTimeMeasure.equals("")) {
            positiveTimeMeasure = "5000";
        }
        if (negativeAmpMeasure.equals("")) {
            negativeAmpMeasure = "300";
        }
        if (positiveAmpMeasure.equals("")) {
            positiveAmpMeasure = "300";
        }
        if (quantityFastPulses.equals("0") &&
                positiveTimeFastWaves.equals("0") &&
                negativeTimeFastWaves.equals("0")) {
            commonFastPulsesTimeEdit.clear();
        } else {
            int commonMeasureTime = Integer.parseInt(quantityFastPulses) *
                    (Integer.parseInt(positiveTimeFastWaves) +
                            Integer.parseInt(negativeTimeFastWaves));
            commonFastPulsesTimeEdit.setText(String.valueOf((double) commonMeasureTime / 1000));
        }
        if (positiveTimeMeasure.equals("0") &&
                negativeTimeMeasure.equals("0")) {
            commonMeasureTimeEdit.clear();
        } else {
            int commonMeasureTime = Integer.parseInt(negativeTimeMeasure) + Integer.parseInt(positiveTimeMeasure);
            commonMeasureTimeEdit.setText(String.valueOf((double) commonMeasureTime / 1000));
        }
        setup.setLeakingTime(Integer.parseInt(waitingTime));
        setup.setPauseTime(Integer.parseInt(pauseTime));
        setup.setFirstPolarityMeasure(!firstPolarityMeasure);
        setup.setFirstPolarityReversal(!firstPolarityReversal);
        setup.setNegativeAmplitudeMeasurePulses(Integer.parseInt(negativeAmpMeasure));
        setup.setPositiveAmplitudeMeasurePulses(Integer.parseInt(positiveAmpMeasure));
        setup.setNegativeAmplitudeFastPolarityPulses(Integer.parseInt(negativeAmpFast));
        setup.setPositiveAmplitudeFastPolarityPulses(Integer.parseInt(positiveAmpFastWaves));
        setup.setQuantityFastPolarityPulses(Integer.parseInt(quantityFastPulses));
        setup.setNegativeMeasureTime(Integer.parseInt(negativeTimeMeasure));
        setup.setPositiveMeasureTime(Integer.parseInt(positiveTimeMeasure));
        setup.setNegativeFastPolarityReversalTime(Integer.parseInt(negativeTimeFastWaves));
        setup.setPositiveFastPolarityReversalTime(Integer.parseInt(positiveTimeFastWaves));
        VisualisationPlot visualisationPlot = new VisualisationPlot(visualPlot);
        visualPlot = visualisationPlot.drawMeasureGraphic(visualPlot, setup);
    }

    public void render(MouseEvent mouseEvent) {
        if (uiValidation.valuesValidation()) {
            renderVisualisation();
        }
        setPlotTooltip();
    }

    public void visualPlotMouseEnter(MouseEvent mouseEvent) {
    }

    public void visualPlotMouseExit(MouseEvent mouseEvent) {
        coordinateLabel.setVisible(false);
    }

    public void visualPlotMouseMoved(MouseEvent mouseEvent) {
    }

    public void xAxisMouseMove(MouseEvent mouseEvent) {
        coordinateLabel.setText(
                String.format(
                        "время = %.2f мс",
                        xTimeVisual.getValueForDisplay(mouseEvent.getX())
                )
        );
    }

    public void yAxisMouseMove(MouseEvent mouseEvent) {
        coordinateLabel.setText(
                String.format(
                        "амплитуда = %.2f мВ",
                        yAmpVisual.getValueForDisplay(mouseEvent.getY())
                )
        );
    }

    private void menuBarSetup() {
        menuBar.getMenus().clear();

        Menu file = new Menu("Файл");

        MenuItem open = new MenuItem("Открыть");
        MenuItem openPoly = new MenuItem("Открыть полярограмму");
        MenuItem openDetails = new MenuItem("Открыть с подробностями");
        MenuItem setup = new MenuItem("Настройки");
        MenuItem close = new MenuItem("Выход");
        open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        setup.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
        close.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        open.setOnAction((event) -> {
            openPlotWindow();
        });
        openPoly.setOnAction((event) -> {
            openPolyWindow();
        });
        openDetails.setOnAction((event) -> {
            FXMLLoader loader = openChooseWindow();
            OpenWindowController openWindowController = loader.getController();
            openWindowController.setNewWindow(true);
        });
        setup.setOnAction((event) -> {
            FXMLLoader loader = openSetupWindow();
            SetupController setupController = loader.getController();
            setupController.setPositiveEnd(this.setup.getPositiveAmplitudeMeasurePulses());
            setupController.setNegativeEnd(this.setup.getNegativeAmplitudeMeasurePulses() * (-1));
            setupController.setMaxNegTime(this.setup.getNegativeMeasureTime());
            setupController.setMaxPosTime(this.setup.getPositiveMeasureTime());
            setupController.setControl(control);
        });
        close.setOnAction((event) -> {
            if (control != null) {
                control.sendOnExit();
                control.closeConnection();
            }
            Platform.exit();
            System.exit(0);
        });

        Menu commands = new Menu("Команды");

        MenuItem sendTest = new MenuItem("Тест");
        MenuItem getStatus = new MenuItem("Получить статус");
        MenuItem getSetup = new MenuItem("Получить настройки");
        MenuItem getPolySetup = new MenuItem("Получить настройки полярограммы");
        MenuItem connection = new MenuItem("Подключение");
        sendTest.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+T"));
        getStatus.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        getPolySetup.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+E"));
        getSetup.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+D"));
        sendTest.setOnAction((event) -> control.sendTest());
        getStatus.setOnAction((event) -> control.sendStatusRequest());
        getSetup.setOnAction((event) -> control.sendSetupRequest());
        getPolySetup.setOnAction((event) -> control.sendPolySetupRequest());
        connection.setOnAction((event) -> connectionAvailable());

        file.getItems().addAll(/*open, openPoly,*/ setup, new SeparatorMenuItem(), openDetails, new SeparatorMenuItem(), close);
        commands.getItems().addAll(sendTest, getStatus, getSetup, getPolySetup, new SeparatorMenuItem(), connection);

        menuBar.getMenus().addAll(file, commands);
    }

    private FXMLLoader openChooseWindow() {
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newWindow.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Открытие ...");
            scene.getStylesheets().add("/styles/labStyle.css");
            stage.setScene(scene);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    stage.close();
                }
            });
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return fxmlLoader;
    }

    private FXMLLoader openSetupWindow() {
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/setup.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Плавный набор напряжения");
            scene.getStylesheets().add("/styles/labStyle.css");
            stage.setScene(scene);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    stage.close();
                }
            });
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return fxmlLoader;
    }

    private void connectionAvailable() {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals("Подключение")) {
                tab.setDisable(false);
            }
        }
        tabPane.getSelectionModel().select(2);
    }

    private void openPlotWindow() {
        try {


            String fileName = ChooseFile.chooseFile(Write.fileMeasurePath);
            MeasurementSetup measurementSetup = Read.reading1(Write.fileMeasurePath + "\\" + fileName);
            FXMLLoader fxmlLoader = newWindow("/fxml/graphMain.fxml", "Измерения глюкозы");
            GraphController graphController = fxmlLoader.getController();
            graphController.addFirstSeries(fileName, measurementSetup);


//            ArrayList<Number> xValues = (ArrayList<Number>) data.getCurrentXMeasurement();
//            ArrayList<Number> yValues = (ArrayList<Number>) data.getCurrentYMeasurement();
//            currentSeries = new XYChart.Series<>();
//            currentSeries.setName(fileName);

//            Platform.runLater(() -> {
//                for (int i = 0; i < xValues.size(); i++) {
//                    currentSeries.getData().add(new XYChart.Data<>(xValues.get(i), yValues.get(i)));
//                }
//                graphController.glucoChart.setAnimated(false);
//                graphController.glucoChart.getData().add(currentSeries);
//                //graphController.legendPane.getChildren().add(currentSeries.getLegend());
//            });

//            Platform.runLater(() -> {
//                for (int i = 0; i < xValues.size(); i++) {
//                    currentSeries.getData().add(new XYChart.Data<>(xValues.get(i), yValues.get(i)));
//                }
//                MultipleSameAxesLineChart multipleSameAxesLineChart = new MultipleSameAxesLineChart(graphController.glucoChart, graphController.stackPane);
//                graphController.glucoChart.setAnimated(false);
//                //graphController.glucoChart.getData().add(currentSeries);
//                multipleSameAxesLineChart.addSeries(currentSeries, Color.RED, fileName);
//                //graphController.legendPane.getChildren().add(currentSeries.getLegend());
//            });


        } catch (IOException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
    }

    private void openPolyWindow() {
        String fileName = null;
        try {
            fileName = ChooseFile.chooseFile(Write.filePolyPath);
            PolySetup polySetup = Read.readingPoly(Write.filePolyPath + "\\" + fileName);
            FXMLLoader fxmlLoader = newWindow("/fxml/polyGraph.fxml", "Полярограмма");
            PolyGraphController polyGraphController = fxmlLoader.getController();
            polyGraphController.addFirstSeries(fileName, polySetup);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
    }

    private FXMLLoader newWindow(String fxml, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Parent root1 = fxmlLoader.load();
            Scene scene = new Scene(root1);
            Stage stage = new Stage();
            stage.setTitle(title);
            scene.getStylesheets().add("/styles/labStyle.css");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    stage.close();
                }
            });
            return fxmlLoader;
            //graphController = fxmlLoader.getController();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;

    }

    public void sendPolyData(ActionEvent actionEvent) {
        if (uiValidation.valuesPolyValidation()) {
            polySetup = uiValidation.renderPolyVisualisation();
            if (comPortConnection != null && comPortConnection.isBusy() /*&& control.isTestOk()*/) {
                control.sendByteArray(polySetup.getTransmitArray());
            }
        }
    }

    public void renderPoly(MouseEvent mouseEvent) {
        if (uiValidation.valuesPolyValidation()) {
            polySetup = uiValidation.renderPolyVisualisation();
        }
    }

    public void sendPolySetupRequest(Event event) {
        filterCheckBox.setVisible(true);
        if (polyTab.isSelected()) {
            realLeakingTimeLabel.setVisible(false);
            if (comPortConnection != null && comPortConnection.isBusy()) {
                control.sendPolySetupRequest();
            }
            logger.debug("sendPolySetupRequest");
        }
    }

    public void sendSetupRequest(Event event) {
        try {
            filterCheckBox.setVisible(false);
        } catch (NullPointerException e) {
            logger.debug(e.toString());
        }
        if (measureTab.isSelected()) {
            if (comPortConnection != null && comPortConnection.isBusy()) {
                control.sendSetupRequest();
            }
            logger.debug("sendSetupRequest");
        }
    }

    public void addFiltration(ActionEvent actionEvent) {
    }

    public void chartClear(ActionEvent actionEvent) {
        glucoChart.getData().clear();
        for (int i = 0; i < graphStackPane.getChildren().size(); i++) {
            if(graphStackPane.getChildren().get(i).getClass().equals(HBox.class)){
                graphStackPane.getChildren().remove(i);
            }
        }
        polyChart.getData().clear();
    }
}
