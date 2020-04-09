package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Button updatePortsButton;    
    @FXML
    public Button openPortButton;
    public ChoiceBox portChoiceBox;
    public ChoiceBox speedChoiceBox;
    public ChoiceBox parityChoiceBox;
    public ChoiceBox stopBitsChoiceBox;
    public ChoiceBox sizeCharChoiceBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void openPort(ActionEvent actionEvent) {
    }

    public void refresh(ActionEvent actionEvent) {
    }
}
