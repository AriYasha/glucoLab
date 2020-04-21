package sample;

import comPort.ComPortConnection;
import entity.MeasurementSetup;
import graph.VisualisationPlot;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    private MeasurementSetup setup;
    private VisualisationPlot visualisationPlot;

    public Label coordinateLabel;
    public Button updatePortsButton;
    public Button openPortButton;
    public ChoiceBox<String> portChoiceBox;
    public ChoiceBox speedChoiceBox;
    public ChoiceBox parityChoiceBox;
    public ChoiceBox<String> stopBitsChoiceBox;
    public ChoiceBox<String> sizeCharChoiceBox;
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

        chartBackground.setOnMouseExited(event -> {
            //coordinateLabel.setVisible(false);
        });

//        for (Node n: chartBackground.getParent().getChildrenUnmodifiable()) {
//            if (n != chartBackground && n != xTimeVisual && n != yAmpVisual) {
//                n.setMouseTransparent(true);
//            }
//        }

        chartSeries.setOnMouseMoved(event -> {
            coordinateLabel.setText(
                    String.format(
                            "(%.2f, %.2f)",
                            xTimeVisual.getValueForDisplay(event.getX()),
                            yAmpVisual.getValueForDisplay(event.getY())
                    )
            );
        });

        chartBackground.setOnMouseMoved(event -> {
            coordinateLabel.setText(
                    String.format(
                            "(%.2f, %.2f)",
                            xTimeVisual.getValueForDisplay(event.getX()),
                            yAmpVisual.getValueForDisplay(event.getY())
                    )
            );
        });

        setTooltip();


    }

    private void setTooltip() {
//        Node chartSeries = visualPlot.lookup(".chart-series-line");
//        chartSeries.setOnMouseMoved(event -> {
//            System.out.println("hi");
//            coordinateLabel.setText(
//                    String.format(
//                            "(%.2f, %.2f)",
//                            visualPlot.getXAxis().getValueForDisplay(event.getX()),
//                            visualPlot.getYAxis().getValueForDisplay(event.getY())
//                    )
//            );
//        });
        ObservableList<XYChart.Data> dataList = ((XYChart.Series) visualPlot.getData().get(0)).getData();
        dataList.forEach(data -> {
            Node node = data.getNode();
            Tooltip tooltip = new Tooltip('(' + data.getXValue().toString() + ';' + data.getYValue().toString() + ')');
            Tooltip.install(node, tooltip);
        });
    }



    public void openPort(ActionEvent actionEvent) {
        String s = (String) speedChoiceBox.getValue();
        System.out.println(s);
    }

    public void refresh(ActionEvent actionEvent) {
        portChoiceBox.getItems().removeAll(portChoiceBox.getItems());
        String[] portNames = ComPortConnection.getPortNames();
        portChoiceBox.getItems().addAll(portNames);
    }

    public void defaultPortSettings(ActionEvent actionEvent) {
        setDisableElements();
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
        setTooltip();
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
        setTooltip();
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
        setTooltip();
    }

    public void visualPlotMouseEnter(MouseEvent mouseEvent) {
        //coordinateLabel.setVisible(true);
        //visualPlot.setCursor(Cursor.CROSSHAIR);
    }

    public void visualPlotMouseExit(MouseEvent mouseEvent) {
        coordinateLabel.setVisible(false);
    }

    public void visualPlotMouseMoved(MouseEvent mouseEvent) {
    }

    public void xAxisMouseMove(MouseEvent mouseEvent) {
        coordinateLabel.setText(
                String.format(
                        "x = %.2f",
                        xTimeVisual.getValueForDisplay(mouseEvent.getX())
                )
        );
    }

    public void yAxisMouseMove(MouseEvent mouseEvent) {
        coordinateLabel.setText(
                String.format(
                        "y = %.2f",
                        yAmpVisual.getValueForDisplay(mouseEvent.getY())
                )
        );
    }
}
