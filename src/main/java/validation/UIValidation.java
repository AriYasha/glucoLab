package validation;


import sample.Controller;

public class UIValidation {

    private Controller controller;

    public UIValidation(Controller controller){
        this.controller = controller;
    }

    public void valuesValidation(){
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
    }

    private boolean isDigit(String string){
        return false;
    }

    public void help(){

    }
}
