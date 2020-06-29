package com.controllers;

import com.comPort.Control;
import com.entity.SmoothVoltage;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class SetupController implements Initializable {

    final private static Logger logger = Logger.getLogger(SetupController.class);

    public TextField positiveTimeEdit;
    public TextField negativeTimeEdit;
    public TextField positiveBeginEdit;
    public TextField positiveEndEdit;
    public TextField negativeBeginEdit;
    public TextField negativeEndEdit;
    public Button cancelButton;
    public Button setupButton;
    public CheckBox enableCheckBox;

    private SmoothVoltage smoothVoltage;
    private Control control;
    private int maxPosTime = 0;
    private int maxNegTime = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        smoothVoltage = new SmoothVoltage();
        enabling(new ActionEvent());
        positiveBeginEdit.setText(String.valueOf(smoothVoltage.getPositiveBegin()));
        negativeBeginEdit.setText(String.valueOf(smoothVoltage.getNegativeBegin()));
        positiveTimeEdit.setText(String.valueOf(smoothVoltage.getPositiveTime()));
        negativeTimeEdit.setText(String.valueOf(smoothVoltage.getNegativeTime()));
    }

    public void setPositiveEnd(int positiveEnd) {
        smoothVoltage.setPositiveEnd(positiveEnd);
        positiveEndEdit.setText(String.valueOf(positiveEnd));
    }

    public void setNegativeEnd(int negativeEnd) {
        smoothVoltage.setNegativeEnd(negativeEnd);
        negativeEndEdit.setText(String.valueOf(negativeEnd));
    }

    public void setMaxPosTime(int maxPosTime) {
        this.maxPosTime = maxPosTime;
    }

    public void setMaxNegTime(int maxNegTime) {
        this.maxNegTime = maxNegTime;
    }

    public void setControl(Control control) {
        this.control = control;
        getSetupFromDevice();
    }

    private void getSetupFromDevice() {
        control.removeListener();
        control.sendSmoothRequest();
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] readArray = control.readSmoothBytes();
        parseArray(readArray);
        control.addListener();
    }

    private void parseArray(byte[] array) {
        if(array[1] == 1){
            enableCheckBox.setSelected(true);
            smoothVoltage.setSetup(true);
        } else{
            enableCheckBox.setSelected(false);
            smoothVoltage.setSetup(false);
        }
        enabling(new ActionEvent());
        byte[] timeBytes = {array[4], array[3], array[2]};
        smoothVoltage.setPositiveTime(control.getIntFromArray(timeBytes));
        positiveTimeEdit.setText(String.valueOf(smoothVoltage.getPositiveTime()));
        timeBytes[0] = array[7];
        timeBytes[1] = array[6];
        timeBytes[2] = array[5];
        smoothVoltage.setNegativeTime(control.getIntFromArray(timeBytes));
        negativeTimeEdit.setText(String.valueOf(smoothVoltage.getNegativeTime()));
        byte[] bytes = {array[11], array[10]};
        int voltage = control.getIntFromArray(bytes);
        voltage = array[9] == 1 ? voltage * (-1) : voltage;
        smoothVoltage.setPositiveBegin(voltage);
        positiveBeginEdit.setText(String.valueOf(smoothVoltage.getPositiveBegin()));
        bytes[0] = array[14];
        bytes[1] = array[13];
        voltage = control.getIntFromArray(bytes);
        voltage = array[12] == 1 ? voltage * (-1) : voltage;
        smoothVoltage.setPositiveEnd(voltage);
        //positiveEndEdit.setText(String.valueOf(smoothVoltage.getPositiveEnd()));
        bytes[0] = array[17];
        bytes[1] = array[16];
        voltage = control.getIntFromArray(bytes);
        voltage = array[15] == 1 ? voltage * (-1) : voltage;
        smoothVoltage.setNegativeBegin(voltage);
        negativeBeginEdit.setText(String.valueOf(smoothVoltage.getNegativeBegin()));
        bytes[0] = array[20];
        bytes[1] = array[19];
        voltage = control.getIntFromArray(bytes);
        voltage = array[18] == 1 ? voltage * (-1) : voltage;
        smoothVoltage.setNegativeEnd(voltage);
        //negativeEndEdit.setText(String.valueOf(smoothVoltage.getNegativeEnd()));
        logger.debug(smoothVoltage.toString());
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void cancel(ActionEvent actionEvent) {
        closeWindow();
    }

    public void setup(ActionEvent actionEvent) {
        try {
            int positiveTime = Integer.parseInt(positiveTimeEdit.getText());
            int negativeTime = Integer.parseInt(negativeTimeEdit.getText());
            if (positiveTime > maxPosTime) {
                positiveTime = maxPosTime;
            }
            if (negativeTime > maxNegTime) {
                negativeTime = maxNegTime;
            }
            smoothVoltage.setPositiveTime(positiveTime);
            smoothVoltage.setNegativeTime(negativeTime);
            smoothVoltage.setPositiveBegin(Integer.parseInt(positiveBeginEdit.getText()));
            smoothVoltage.setPositiveEnd(Integer.parseInt(positiveEndEdit.getText()));
            smoothVoltage.setNegativeBegin(Integer.parseInt(negativeBeginEdit.getText()));
            smoothVoltage.setNegativeEnd(Integer.parseInt(negativeEndEdit.getText()));
            smoothVoltage.setSetup(enableCheckBox.isSelected());
            control.sendByteArray(smoothVoltage.getTransmitArray());
            closeWindow();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Упс...");
            alert.setHeaderText("Что-то пошло не так");
            alert.setContentText("Проверьте правильность введённых данных");
            alert.show();
        }

    }

    public void enabling(ActionEvent actionEvent) {
        if (enableCheckBox.isSelected()) {
            positiveBeginEdit.setDisable(false);
            negativeBeginEdit.setDisable(false);
            positiveTimeEdit.setDisable(false);
            negativeTimeEdit.setDisable(false);
        } else {
            positiveBeginEdit.setDisable(true);
            negativeBeginEdit.setDisable(true);
            positiveTimeEdit.setDisable(true);
            negativeTimeEdit.setDisable(true);
        }
    }
}
