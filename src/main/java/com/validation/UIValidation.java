package com.validation;


import com.sample.Controller;
import javafx.application.Platform;
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

    public void setTabImages(){
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
}
