package validation;


import comPort.ComPortConnection;
import entity.MeasurementSetup;
import graph.VisualisationPlot;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import sample.Controller;


public class UIValidation {

    private Controller controller;

    public UIValidation(Controller controller) {
        this.controller = controller;
    }

    public boolean valuesValidation() {
        boolean isValid = false;
        String waitingTime = controller.waitingTimeEdit.getText();
        System.out.println(isDigit(waitingTime));
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
        if (    isDigit(waitingTime) &&
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
        }
//        if(Integer.parseInt(waitingTime) < 0 &&
//                Integer.parseInt(pauseTime) > 32000){
//
//
//        }
        return isValid;
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
            isDigit = false;
        }
        return isDigit;
    }

    public void help() {

    }
}
