package sample;

import com.fazecast.jSerialComm.SerialPort;
import comPort.ComPortConnection;
import comPort.Control;
import entity.MeasurementSetup;
import exception.ComPortException;
import graph.MultipleAxesLineChart;
import graph.VisualisationPlot;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

public class Controller implements Initializable {


    public Tab setupTab;
    public LineChart glucoChart;
    public AnchorPane graphPane;
    public NumberAxis glocoTimeAxis;
    public NumberAxis glucoAmpAxis;
    public StackPane graphStackPane;
    public AnchorPane legendPane;
    private MeasurementSetup setup;
    private VisualisationPlot visualisationPlot;
    private ComPortConnection comPortConnection;
    private Control control;

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
        //control = new Control();
        Image picture = new Image("images/red Ball.png", true);
        connectImage.setImage(picture);
        setTooltips();
        portChoiceBox.getItems().removeAll(portChoiceBox.getItems());
        String[] portNames = ComPortConnection.getPortNames();
        portChoiceBox.getItems().addAll(portNames);
        portChoiceBox.setValue(portNames[0]);
        speedChoiceBox.getItems().removeAll(speedChoiceBox.getItems());
        speedChoiceBox.getItems().addAll("9600", "19200", "38400", "57600", "115200");
        speedChoiceBox.setValue("9600");
        parityChoiceBox.getItems().removeAll(parityChoiceBox.getItems());
        parityChoiceBox.getItems().addAll("no", "odd", "even", "mark", "space");
        parityChoiceBox.setValue("no");
        stopBitsChoiceBox.getItems().removeAll(stopBitsChoiceBox.getItems());
        stopBitsChoiceBox.getItems().addAll("1", "1,5", "2");
        stopBitsChoiceBox.setValue("1");
        sizeCharChoiceBox.getItems().removeAll(sizeCharChoiceBox.getItems());
        sizeCharChoiceBox.getItems().addAll("8", "9", "7", "6", "5", "4", "3", "2", "1");
        sizeCharChoiceBox.setValue("8");
        defaultPortCheckBox.setSelected(true);
        setDisableElements();
        setup = new MeasurementSetup();
        visualisationPlot = new VisualisationPlot(visualPlot);
        renderVisualization();
        coordinateLabel.setVisible(false);

        Node chartBackground = visualPlot.lookup(".chart-plot-background");
        Node chartSeries = visualPlot.lookup(".chart-series-line");
        System.out.println(chartSeries.toString());
        chartBackground.setOnMouseEntered(event -> {
            visualPlot.setCursor(Cursor.CROSSHAIR);
            coordinateLabel.setVisible(true);
        });

        chartSeries.setOnMouseMoved(event -> {
            coordinateLabel.setText(
                    String.format(
                            "время = %.2f мс, амплитуда = %.2f мВ",
                            xTimeVisual.getValueForDisplay(event.getX()),
                            yAmpVisual.getValueForDisplay(event.getY())
                    )
            );
        });

        chartBackground.setOnMouseMoved(event -> {
            coordinateLabel.setText(
                    String.format(
                            "время = %.2f мс,%nамплитуда = %.2f мВ",
                            xTimeVisual.getValueForDisplay(event.getX()),
                            yAmpVisual.getValueForDisplay(event.getY())
                    )
            );
        });
        setPlotTooltip();
        setConnection();
    }

    private void setPlotTooltip() {
        ObservableList<XYChart.Data> dataList = ((XYChart.Series) visualPlot.getData().get(0)).getData();
        dataList.forEach(data -> {
            Node node = data.getNode();
            Tooltip tooltip = new Tooltip("Время = " + data.getXValue().toString() + ", мс\nАмплитуда = " + data.getYValue().toString() + ", мВ");
            Tooltip.install(node, tooltip);
        });
    }

    private void setConnection() {
        Runnable task = () -> {
            ObservableList portList = portChoiceBox.getItems();
            byte[] readBytes = new byte[5];
            for (Object port : portList) {
                try {
                    comPortConnection = ComPortConnection.getInstance((String) port);
                    System.out.println((String) port);
                    comPortConnection.openPort();
                    control = new Control(comPortConnection);
                    //control.setComPortConnection(comPortConnection);
                    control.sendTest();
                    System.out.println(comPortConnection.toString());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    readBytes = control.readBytes();
                    byte[] controlBytes = {85, 79, 104, -73, -86};
                    if (Arrays.equals(readBytes, controlBytes)) {
                        Image picture = new Image("images/green Ball.png", true);
                        connectImage.setImage(picture);
                        Platform.runLater(() -> connectionLabel.setText("Подключено"));
                        Platform.runLater(() -> openPortButton.setText("Прервать соединение"));
                        openPortButton.setSelected(true);
                        control.addListener();
                        break;
                    } else {
                        comPortConnection.close();
                    }
                    System.out.println(Arrays.toString(readBytes));

                } catch (ComPortException e) {
                    connectionLabel.setText("Не удалось подключиться автоматически");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        /*new Thread(() -> {
            ObservableList portList = portChoiceBox.getItems();
            byte[] readBytes = new byte[5];
            for (Object port : portList) {
                try {
                    comPortConnection = ComPortConnection.getInstance((String) port);
                    System.out.println((String) port);
                    comPortConnection.openPort();
                    control = new Control(comPortConnection);
                    control.sendTest();
                    System.out.println(comPortConnection.toString());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    readBytes = control.readBytes();
                    System.out.println(Arrays.toString(readBytes));
                    comPortConnection = null;
                } catch (ComPortException e) {
                    connectionLabel.setText("Не удалось подключиться автоматически");
                }
                comPortConnection = null;
            }
        }).start();*/
        Thread thread = new Thread(task);
        thread.start();
    }


    public void openPort(ActionEvent actionEvent) {
        if (defaultPortCheckBox.isSelected() && openPortButton.isSelected()) {
            try {
                comPortConnection = ComPortConnection.getInstance(ComPortConnection.getPortName());
                comPortConnection.openPort();
                control = new Control(comPortConnection);
                Image picture = new Image("images/green Ball.png", true);
                connectImage.setImage(picture);
                connectionLabel.setText("Подключено");
                openPortButton.setText("Прервать соединение");
            } catch (ComPortException e) {
                sendError(e);
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
                control = new Control(comPortConnection);
                Image picture = new Image("images/green Ball.png", true);
                connectImage.setImage(picture);
                connectionLabel.setText("Подключено");
                openPortButton.setText("Прервать соединение");
            } catch (ComPortException e) {
                sendError(e);
            }

        } else if (!openPortButton.isSelected()) {
            try {
                comPortConnection.close();
                openPortButton.setText("Старт соединения");
                Image picture = new Image("images/red Ball.png", true);
                connectImage.setImage(picture);
                connectionLabel.setText("Не подключено");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (comPortConnection.isBusy()) {
            control.sendByte((byte) 170);
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
        Tooltip waitingTimeTooltip = new Tooltip("Время протекания, Тпр");
        waitingTimeEdit.setTooltip(waitingTimeTooltip);
        Tooltip.install(waitingTimeEdit, waitingTimeTooltip);
        Tooltip pauseTimeTooltip = new Tooltip("Время паузы, Тпаузы");
        pauseTimeEdit.setTooltip(pauseTimeTooltip);
        Tooltip.install(pauseTimeEdit, pauseTimeTooltip);
        Tooltip positiveTimeFastWavesTooltip = new Tooltip(" t+ ");
        positiveTimeFastWavesEdit.setTooltip(positiveTimeFastWavesTooltip);
        Tooltip.install(positiveTimeFastWavesEdit, positiveTimeFastWavesTooltip);
        Tooltip negativeTimeFastWavesTooltip = new Tooltip(" t- ");
        negativeTimeFastWavesEdit.setTooltip(negativeTimeFastWavesTooltip);
        Tooltip.install(negativeTimeFastWavesEdit, negativeTimeFastWavesTooltip);
        Tooltip positiveAmpFastWavesTooltip = new Tooltip("u+");
        positiveAmpFastWavesEdit.setTooltip(positiveAmpFastWavesTooltip);
        Tooltip.install(positiveAmpFastWavesEdit, positiveAmpFastWavesTooltip);
        Tooltip negativeAmpFastWavesTooltip = new Tooltip("u-");
        negativeAmpFastWavesEdit.setTooltip(negativeAmpFastWavesTooltip);
        Tooltip.install(negativeAmpFastWavesEdit, negativeAmpFastWavesTooltip);
        Tooltip positiveTimeMeasureTooltip = new Tooltip(" T+ ");
        positiveTimeMeasureEdit.setTooltip(positiveTimeMeasureTooltip);
        Tooltip.install(positiveTimeMeasureEdit, positiveTimeMeasureTooltip);
        Tooltip negativeTimeMeasureTooltip = new Tooltip(" T- ");
        negativeTimeMeasureEdit.setTooltip(negativeTimeMeasureTooltip);
        Tooltip.install(negativeTimeMeasureEdit, negativeTimeMeasureTooltip);
        Tooltip positiveAmpMeasureTooltip = new Tooltip("U+");
        positiveAmpMeasureEdit.setTooltip(positiveAmpMeasureTooltip);
        Tooltip.install(positiveAmpMeasureEdit, positiveAmpMeasureTooltip);
        Tooltip negativeAmpMeasureTooltip = new Tooltip("U-");
        negativeAmpMeasureEdit.setTooltip(negativeAmpMeasureTooltip);
        Tooltip.install(negativeAmpMeasureEdit, negativeAmpMeasureTooltip);
        Tooltip quantityFastPulsesTooltip = new Tooltip(" N ");
        quantityFastPulsesEdit.setTooltip(quantityFastPulsesTooltip);
        Tooltip.install(quantityFastPulsesEdit, quantityFastPulsesTooltip);
        Tooltip commonFastPulsesTimeTooltip = new Tooltip(" Tп ");
        commonFastPulsesTimeEdit.setTooltip(commonFastPulsesTimeTooltip);
        Tooltip.install(commonFastPulsesTimeEdit, commonFastPulsesTimeTooltip);
        Tooltip commonMeasureTimeTooltip = new Tooltip(" Tизм ");
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
        if (comPortConnection.isBusy()) {
            control.sendByteArray(setup.getTransmitArray());
        }

        tabPane.getSelectionModel().selectNext();
        glucoChart.getData().add(prepareSeries("Напряжение", (x) -> (double) x * x));

        MultipleAxesLineChart voltageChart = new MultipleAxesLineChart(glucoChart, graphStackPane);
        voltageChart.addSeries(prepareSeries("Ток", (x) -> (double) x), Color.GREEN);
        legendPane.getChildren().add(voltageChart.getLegend());
    }

    private XYChart.Series<Number, Number> prepareSeries(String name, Function<Integer, Double> function) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (int i = 0; i < 36; i++) {
            series.getData().add(new XYChart.Data<>(i, function.apply(i)));
        }
        return series;
    }


    public void disablePauseTime(ActionEvent actionEvent) {
        checkTimeAvailability(pauseTimeCheckBox, pauseTimeEdit, pauseTimeLabel);
    }

    public void commonMeasureTimeAction(ActionEvent actionEvent) {
        commonMeasure(positiveTimeMeasureEdit, negativeTimeMeasureEdit);
    }

    public void commonMeasureTime(MouseEvent mouseEvent) {
        commonMeasure(positiveTimeMeasureEdit, negativeTimeMeasureEdit);
        renderVisualization();
        setPlotTooltip();
    }

    private void commonMeasure(TextField textFieldPos, TextField textFieldNeg) {
        String negativeTimeMeasure = textFieldNeg.getText();
        String positiveTimeMeasure = textFieldPos.getText();
        if (positiveTimeMeasure.equals("")) {
            positiveTimeMeasure = "0";
        }
        if (negativeTimeMeasure.equals("")) {
            negativeTimeMeasure = "0";
        }
        if (positiveTimeMeasure.equals("0") &&
                negativeTimeMeasure.equals("0")) {
            commonMeasureTimeEdit.clear();
        } else {
            int commonMeasureTime = Integer.parseInt(negativeTimeMeasure) + Integer.parseInt(positiveTimeMeasure);
            commonMeasureTimeEdit.setText(String.valueOf((double) commonMeasureTime / 1000));
        }
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

    public void commonFastPulsesTime(MouseEvent mouseEvent) {
        String negativeTimeFastWaves = negativeTimeFastWavesEdit.getText();
        String positiveTimeFastWaves = positiveTimeFastWavesEdit.getText();
        String quantityFastPulses = quantityFastPulsesEdit.getText();
        if (negativeTimeFastWaves.equals("")) {
            negativeTimeFastWaves = "0";
        }
        if (positiveTimeFastWaves.equals("")) {
            positiveTimeFastWaves = "0";
        }
        if (quantityFastPulses.equals("")) {
            quantityFastPulses = "0";
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
        renderVisualization();
        setPlotTooltip();
    }

    private void renderVisualization() {
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
        setup.setLeakingTime(Integer.parseInt(waitingTime));
        setup.setPauseTime(Integer.parseInt(pauseTime));
        setup.setFirstPolarityMeasure(firstPolarityMeasure);
        setup.setFirstPolarityReversal(firstPolarityReversal);
        setup.setNegativeAmplitudeMeasurePulses(Integer.parseInt(negativeAmpMeasure));
        setup.setPositiveAmplitudeMeasurePulses(Integer.parseInt(positiveAmpMeasure));
        setup.setNegativeAmplitudeFastPolarityPulses(Integer.parseInt(negativeAmpFast));
        setup.setPositiveAmplitudeFastPolarityPulses(Integer.parseInt(positiveAmpFastWaves));
        setup.setQuantityFastPolarityPulses(Integer.parseInt(quantityFastPulses));
        setup.setNegativeMeasureTime(Integer.parseInt(negativeTimeMeasure));
        setup.setPositiveMeasureTime(Integer.parseInt(positiveTimeMeasure));
        setup.setNegativeFastPolarityReversalTime(Integer.parseInt(negativeTimeFastWaves));
        setup.setPositiveFastPolarityReversalTime(Integer.parseInt(positiveTimeFastWaves));
        visualPlot = visualisationPlot.drawGraphic(visualPlot, setup);
    }

    public void render(MouseEvent mouseEvent) {
        renderVisualization();
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
                        "время - %.2f мс",
                        xTimeVisual.getValueForDisplay(mouseEvent.getX())
                )
        );
    }

    public void yAxisMouseMove(MouseEvent mouseEvent) {
        coordinateLabel.setText(
                String.format(
                        "амплитуда - %.2f мВ",
                        yAmpVisual.getValueForDisplay(mouseEvent.getY())
                )
        );
    }
}
