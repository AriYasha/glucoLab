package sample;

import comPort.ComPortConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Button updatePortsButton;
    public Button openPortButton;
    public ChoiceBox portChoiceBox;
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
    public TextField amplitudeFastWavesEdit;
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

    public void commonMeasureTime(MouseEvent actionEvent) {
        commonMeasure(positiveTimeMeasureEdit, negativeTimeMeasureEdit);
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
    }
}
