package sample;

import comPort.ComPortConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

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
    public LineChart mainDiagram;
    public RadioButton negativeMeasureHalfWave;
    public ToggleGroup choiceBigWave;
    public RadioButton positiveMeasureHalfWave;
    public RadioButton positiveFastHalfWave;
    public ToggleGroup choiceLittleWave;
    public RadioButton negativeFastHalfWave;
    public TextField pauseTimeEdit;
    public TextField leakingTimeEdit;
    public TextField positiveTimeFastWavesEdit;
    public TextField negativeTimeFastWavesEdit;
    public TextField fastWavesFullTimeEdit;
    public TextField wavesQuantityEdit;
    public TextField positiveAmplitudeFastWavesEdit;
    public TextField negativeAmplitudeFastWavesEdit;
    public Label connectionLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        portChoiceBox.getItems().removeAll(portChoiceBox.getItems());
        String [] portNames = ComPortConnection.getPortNames();
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
        String [] portNames = ComPortConnection.getPortNames();
        portChoiceBox.getItems().addAll(portNames);
    }

    public void defaultPortSettings(ActionEvent actionEvent) {
        setDisableElements();
    }

    private void setDisableElements(){
        if(defaultPortCheckBox.isSelected()){
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


}
