package validation;

import comPort.Control;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import sample.Controller;

import java.io.IOException;
import java.util.List;

public class DataFromComPortValidation {

    private Controller controller;

    public DataFromComPortValidation(Controller controller) {
        this.controller = controller;
    }

    public void checkCmd(List<Byte> command) throws IOException {
        if (isCommand(command)) {
//            Platform.runLater(() -> controller.connectionLabel.setText("bye"));
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
            }
            System.out.println("hello");
        } else if (isData(command)) {

        }

    }

    private void statusChecker(byte command) {
        switch (command) {
            case Control.NOT_CONNECTED_STAT:
                break;
            case Control.CONNECTED_STAT:
                break;
            case Control.STRIP_WAITING_STAT:
                break;
            case Control.STRIP_INSERTED_STAT:
                break;
            case Control.DROP_WAITING_STAT:
                break;
            case Control.DROP_DETECTED_STAT:
                break;
            case Control.LEAK_WAITING_STAT:
                break;
            case Control.LEAKING_STAT:
                break;
            case Control.FAST_POLARITY_BEGIN_STAT:
                break;
            case Control.FAST_POLARITY_END_STAT:
                break;
            case Control.START_MEASURE_STAT:
                break;
            case Control.POLARITY_CHANGED_STAT:
                break;
            case Control.END_MEASURE_STAT:
                break;

        }

    }

    private void errorChecker(byte command) {

    }

    private void stripChecker(byte command) {

    }

    private boolean isCommand(List<Byte> command) {
        return command.get(0).equals(Control.START_CMD) &&
                command.get(4).equals(Control.END_CMD) &&
                (byte) (command.get(1) + command.get(2)) == command.get(3);
    }

    private boolean isData(List<Byte> command) {
        return command.get(0).equals(Control.END_CMD) &&
                command.get(4).equals(Control.END_CMD) &&
                (byte) (command.get(1) + command.get(2)) == command.get(3);
    }
}
