package com.validation;


import com.comPort.ComPortConnection;
import com.entity.PolySetup;
import com.graph.VisualisationPlot;
import com.controllers.Controller;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class UIValidation {

    static final private int MAX_TIME_VALUE = 32000;
    static final private int MAX_AMP_VALUE = 1000;
    static final private int MAX_QUANTITY_VALUE = 1000;

    private Controller controller;

    public UIValidation(Controller controller) {
        this.controller = controller;
    }

    public boolean valuesValidation() {
        boolean isValid = false;
        String waitingTime = controller.waitingTimeEdit.getText();
        String pauseTime = controller.pauseTimeEdit.getText();
        String negativeTimeFastWaves = controller.negativeTimeFastWavesEdit.getText();
        String positiveTimeFastWaves = controller.positiveTimeFastWavesEdit.getText();
        String quantityFastPulses = controller.quantityFastPulsesEdit.getText();
        String negativeAmpFast = controller.negativeAmpFastWavesEdit.getText();
        String positiveAmpFastWaves = controller.positiveAmpFastWavesEdit.getText();
        String negativeTimeMeasure = controller.negativeTimeMeasureEdit.getText();
        String positiveTimeMeasure = controller.positiveTimeMeasureEdit.getText();
        String negativeAmpMeasure = controller.negativeAmpMeasureEdit.getText();
        String positiveAmpMeasure = controller.positiveAmpMeasureEdit.getText();
        if (isDigit(waitingTime) &&
                isDigit(pauseTime) &&
                isDigit(negativeAmpFast) &&
                isDigit(negativeAmpMeasure) &&
                isDigit(negativeTimeFastWaves) &&
                isDigit(negativeTimeMeasure) &&
                isDigit(positiveAmpFastWaves) &&
                isDigit(positiveAmpMeasure) &&
                isDigit(positiveTimeFastWaves) &&
                isDigit(positiveTimeMeasure) &&
                isDigit(quantityFastPulses)
                ) {
            isValid = true;
            hideErrorLabels();
        }
        if (isValid) {
            if (!waitingTime.isEmpty() && checkRange(waitingTime, MAX_TIME_VALUE)) {
                showErrorLabel(controller.waitingErrorLabel);
                isValid = false;
            }
            if (!pauseTime.isEmpty() && checkRange(pauseTime, MAX_TIME_VALUE)) {
                showErrorLabel(controller.pauseErrorLabel);
                isValid = false;
            }
            if (!positiveTimeFastWaves.isEmpty() && checkRange(positiveTimeFastWaves, MAX_TIME_VALUE)) {
                showErrorLabel(controller.positiveTimeFastErrorLabel);
                isValid = false;
            }
            if (!negativeTimeFastWaves.isEmpty() && checkRange(negativeTimeFastWaves, MAX_TIME_VALUE)) {
                showErrorLabel(controller.negativeTimeFastErrorLabel);
                isValid = false;
            }
            if (!positiveTimeMeasure.isEmpty() && checkRange(positiveTimeMeasure, MAX_TIME_VALUE)) {
                showErrorLabel(controller.positiveTimeMeasureErrorLabel);
                isValid = false;
            }
            if (!negativeTimeMeasure.isEmpty() && checkRange(negativeTimeMeasure, MAX_TIME_VALUE)) {
                showErrorLabel(controller.negativeTimeMeasureErrorLabel);
                isValid = false;
            }
            if (!positiveAmpFastWaves.isEmpty() && checkRange(positiveAmpFastWaves, MAX_AMP_VALUE)) {
                showErrorLabel(controller.positiveAmpFastErrorLabel);
                isValid = false;
            }
            if (!negativeAmpFast.isEmpty() && checkRange(negativeAmpFast, MAX_AMP_VALUE)) {
                showErrorLabel(controller.negativeAmpFastErrorLabel);
                isValid = false;
            }
            if (!positiveAmpMeasure.isEmpty() && checkRange(positiveAmpMeasure, MAX_AMP_VALUE)) {
                showErrorLabel(controller.positiveAmpMeasureErrorLabel);
                isValid = false;
            }
            if (!negativeAmpMeasure.isEmpty() && checkRange(negativeAmpMeasure, MAX_AMP_VALUE)) {
                showErrorLabel(controller.negativeAmpMeasureErrorLabel);
                isValid = false;
            }
            if (!quantityFastPulses.isEmpty() && checkRange(quantityFastPulses, MAX_QUANTITY_VALUE)) {
                showErrorLabel(controller.quantityErorLabel);
                isValid = false;
            }
        }
        return isValid;
    }

    public boolean valuesPolyValidation() {
        boolean isValid = false;
        String beginPoint = controller.beginPointEdit.getText();
        String mediumPoint = controller.mediumPointEdit.getText();
        String lastPoint = controller.lastPointEdit.getText();
        String quantityReapeted = controller.quantityReapetedEdit.getText();
        String increaseTime = controller.increaseTimeEdit.getText();
        String decreaseTime = controller.decreaseTimeEdit.getText();
        if (    isDigit(beginPoint) &&
                isDigit(mediumPoint) &&
                isDigit(lastPoint) &&
                isDigit(quantityReapeted) &&
                isDigit(increaseTime) &&
                isDigit(decreaseTime)
                ) {
            isValid = true;
            hideErrorLabels();
        }
        if (isValid) {
            if (!beginPoint.isEmpty() && checkRange(beginPoint, -1000, 1000)) {
                showErrorLabel(controller.beginPointError);
                isValid = false;
            }
            if (!mediumPoint.isEmpty() && checkRange(mediumPoint, -1000, 1000)) {
                showErrorLabel(controller.mediumPointError);
                isValid = false;
            }
            if (!lastPoint.isEmpty() && checkRange(lastPoint, -1000, 1000)) {
                showErrorLabel(controller.lastPointError);
                isValid = false;
            }
            if (!increaseTime.isEmpty() && checkRange(increaseTime, 300)) {
                showErrorLabel(controller.increaseTimeError);
                isValid = false;
            }
            if (!decreaseTime.isEmpty() && checkRange(decreaseTime, 300)) {
                showErrorLabel(controller.decreaseTimeError);
                isValid = false;
            }
            if (!quantityReapeted.isEmpty() && checkRange(quantityReapeted, 1, 20)) {
                showErrorLabel(controller.quantityReapetedError);
                isValid = false;
            }
        }
        return isValid;
    }

    public void hideErrorLabels() {
        Platform.runLater(() -> {
            controller.waitingErrorLabel.setVisible(false);
            controller.pauseErrorLabel.setVisible(false);
            controller.positiveTimeFastErrorLabel.setVisible(false);
            controller.negativeTimeFastErrorLabel.setVisible(false);
            controller.positiveTimeMeasureErrorLabel.setVisible(false);
            controller.negativeTimeMeasureErrorLabel.setVisible(false);
            controller.positiveAmpFastErrorLabel.setVisible(false);
            controller.negativeAmpFastErrorLabel.setVisible(false);
            controller.quantityErorLabel.setVisible(false);
            controller.positiveAmpMeasureErrorLabel.setVisible(false);
            controller.negativeAmpMeasureErrorLabel.setVisible(false);

            controller.increaseTimeError.setVisible(false);
            controller.decreaseTimeError.setVisible(false);
            controller.beginPointError.setVisible(false);
            controller.mediumPointError.setVisible(false);
            controller.lastPointError.setVisible(false);
            controller.quantityReapetedError.setVisible(false);
        });
    }

    private void showErrorLabel(Label label) {
        Platform.runLater(() -> {
            label.setVisible(true);
        });
    }

    private boolean checkRange(String string, int secondValue) {
        return Integer.parseInt(string) < 0 ||
                Integer.parseInt(string) > secondValue;

    }

    private boolean checkRange(String string, int firstValue, int secondValue) {
        return Integer.parseInt(string) < firstValue ||
                Integer.parseInt(string) > secondValue;

    }

    private boolean isDigit(String string) {
        boolean isDigit;
        try {
            int a = Integer.parseInt(string);
            isDigit = true;
        } catch (NumberFormatException e) {
            isDigit = false;
        }
        if (string.equals("")) {
            isDigit = true;
        }
        return isDigit;
    }

    public void setOnConnected() {
        Platform.runLater(() -> {
            Image picture = new Image("images/green Ball.png", true);
            controller.connectImage.setImage(picture);
            controller.connectionLabel.setText("Подключено");
            controller.deviceStatus.setText("Ожидание полоски");
            controller.openPortButton.setText("Прервать соединение");
        });
    }

    public void setOnDisconnected() {
        Platform.runLater(() -> {
            Image picture = new Image("images/red Ball.png", true);
            controller.connectImage.setImage(picture);
            controller.connectionLabel.setText("Не подключено");
        });
    }

    private void setTabImages() {
        Platform.runLater(() -> {
            controller.measureTab.setGraphic(buildImage("images/measure.png", 27));
            controller.polyTab.setGraphic(buildImage("images/Hysteresis.png", 27));
            controller.setupTab.setGraphic(buildImage("images/settings.png", 27));
            controller.graphTab.setGraphic(buildImage("images/graph.png", 27));
        });
    }

    private static ImageView buildImage(String imgPatch, int size) {
        Image i = new Image(imgPatch);
        ImageView imageView = new ImageView();
        //You can set width and height
        imageView.setFitHeight(size);
        imageView.setFitWidth(size);
        imageView.setImage(i);
        return imageView;
    }

    public void setImages() {
        setTabImages();
        Platform.runLater(() -> {
            Image picture = new Image("images/red Ball.png", true);
            Image stripType = new Image("images/stripNoName.jpg", true);
            Image send = new Image("images/install.png", true);
            Image sendPoly = new Image("images/install.png", true);
            controller.stripTypeLabel.setVisible(false);
            controller.connectImage.setImage(picture);
            controller.sendImage.setImage(send);
            controller.sendPolyImage.setImage(sendPoly);
            controller.stripTypeImage.setImage(stripType);
        });
    }

    public void connectionSetup() {
        Platform.runLater(() -> {
            controller.portChoiceBox.getItems().removeAll(controller.portChoiceBox.getItems());
            String[] portNames = ComPortConnection.getPortNames();
            controller.portChoiceBox.getItems().addAll(portNames);
            controller.portChoiceBox.setValue(portNames[0]);
            controller.speedChoiceBox.getItems().removeAll(controller.speedChoiceBox.getItems());
            controller.speedChoiceBox.getItems().addAll("9600", "19200", "38400", "57600", "115200");
            controller.speedChoiceBox.setValue("9600");
            controller.parityChoiceBox.getItems().removeAll(controller.parityChoiceBox.getItems());
            controller.parityChoiceBox.getItems().addAll("no", "odd", "even", "mark", "space");
            controller.parityChoiceBox.setValue("no");
            controller.stopBitsChoiceBox.getItems().removeAll(controller.stopBitsChoiceBox.getItems());
            controller.stopBitsChoiceBox.getItems().addAll("1", "1,5", "2");
            controller.stopBitsChoiceBox.setValue("1");
            controller.sizeCharChoiceBox.getItems().removeAll(controller.sizeCharChoiceBox.getItems());
            controller.sizeCharChoiceBox.getItems().addAll("8", "9", "7", "6", "5", "4", "3", "2", "1");
            controller.sizeCharChoiceBox.setValue("8");
            controller.defaultPortCheckBox.setSelected(true);
        });
    }

    public void visualisationSetup(LineChart lineChart, NumberAxis xAxis, NumberAxis yAxis){
        Platform.runLater(() -> {
            Node chartBackground = lineChart.lookup(".chart-plot-background");
            Node chartSeries = lineChart.lookup(".chart-series-line");
            chartBackground.setOnMouseEntered(event -> {
                lineChart.setCursor(Cursor.CROSSHAIR);
                controller.coordinateLabel.setVisible(true);
            });

            chartSeries.setOnMouseMoved(event -> {
                controller.coordinateLabel.setText(
                        String.format(
                                "время = %.2f мс, амплитуда = %.2f мВ",
                                xAxis.getValueForDisplay(event.getX()),
                                yAxis.getValueForDisplay(event.getY())
                        )
                );
            });

            chartBackground.setOnMouseMoved(event -> {
                controller.coordinateLabel.setText(
                        String.format(
                                "время = %.2f мс,%nамплитуда = %.2f мВ",
                                xAxis.getValueForDisplay(event.getX()),
                                yAxis.getValueForDisplay(event.getY())
                        )
                );
            });
        });
    }

    public PolySetup renderPolyVisualisation(){
        String beginPoint = controller.beginPointEdit.getText();
        String mediumPoint = controller.mediumPointEdit.getText();
        String lastPoint = controller.lastPointEdit.getText();
        String quantityReapeted = controller.quantityReapetedEdit.getText();
        String increaseTime = controller.increaseTimeEdit.getText();
        String decreaseTime = controller.decreaseTimeEdit.getText();
        if (beginPoint.equals("")) {
            beginPoint = "-300";
        }
        if (mediumPoint.equals("")) {
            mediumPoint = "300";
        }
        if (lastPoint.equals("")) {
            lastPoint = "-300";
        }
        if (quantityReapeted.equals("")) {
            quantityReapeted = "1";
        }
        if (increaseTime.equals("")) {
            increaseTime = "5";
        }
        if (decreaseTime.equals("")) {
            decreaseTime = "5";
        }
        PolySetup setup = new PolySetup();
        setup.setBeginPoint(Integer.parseInt(beginPoint));
        setup.setMediumPoint(Integer.parseInt(mediumPoint));
        setup.setLastPoint(Integer.parseInt(lastPoint));
        setup.setQuantityReapeted(Integer.parseInt(quantityReapeted));
        setup.setIncreaseTime(Integer.parseInt(increaseTime));
        setup.setDecreaseTime(Integer.parseInt(decreaseTime));
        Platform.runLater(() -> {
            VisualisationPlot visualisationPlot = new VisualisationPlot(controller.visualPolyPlot);
            controller.visualPolyPlot = visualisationPlot.drawPolyGraphic(controller.visualPolyPlot, setup);
        });
        return setup;
    }
}
