package entity;

public class MeasurementSetup {
    private int leakingTime;
    private int pauseTime;

    private int positiveFastPolarityReversalTime;
    private int negativeFastPolarityReversalTime;
    private boolean firstPolarityReversal;
    private int negativeAmplitudeFastPolarityPulses;
    private int positiveAmplitudeFastPolarityPulses;
    private int quantityFastPolarityPulses;

    private int positiveMeasureTime;
    private int negativeMeasureTime;
    private boolean firstPolarityMeasure;
    private int positiveAmplitudeMeasurePulses;
    private int negativeAmplitudeMeasurePulses;

    public MeasurementSetup() {
    }

    public MeasurementSetup(int leakingTime, int pauseTime, int positiveFastPolarityReversalTime, int negativeFastPolarityReversalTime, boolean firstPolarityReversal, int negativeAmplitudeFastPolarityPulses, int positiveAmplitudeFastPolarityPulses, int quantityFastPolarityPulses, int positiveMeasureTime, int negativeMeasureTime, boolean firstPolarityMeasure, int positiveAmplitudeMeasurePulses, int negativeAmplitudeMeasurePulses) {
        this.leakingTime = leakingTime;
        this.pauseTime = pauseTime;
        this.positiveFastPolarityReversalTime = positiveFastPolarityReversalTime;
        this.negativeFastPolarityReversalTime = negativeFastPolarityReversalTime;
        this.firstPolarityReversal = firstPolarityReversal;
        this.negativeAmplitudeFastPolarityPulses = negativeAmplitudeFastPolarityPulses;
        this.positiveAmplitudeFastPolarityPulses = positiveAmplitudeFastPolarityPulses;
        this.quantityFastPolarityPulses = quantityFastPolarityPulses;
        this.positiveMeasureTime = positiveMeasureTime;
        this.negativeMeasureTime = negativeMeasureTime;
        this.firstPolarityMeasure = firstPolarityMeasure;
        this.positiveAmplitudeMeasurePulses = positiveAmplitudeMeasurePulses;
        this.negativeAmplitudeMeasurePulses = negativeAmplitudeMeasurePulses;
    }

    public int getNegativeAmplitudeFastPolarityPulses() {
        return negativeAmplitudeFastPolarityPulses;
    }

    public void setNegativeAmplitudeFastPolarityPulses(int negativeAmplitudeFastPolarityPulses) {
        this.negativeAmplitudeFastPolarityPulses = negativeAmplitudeFastPolarityPulses;
    }

    public int getPositiveAmplitudeFastPolarityPulses() {
        return positiveAmplitudeFastPolarityPulses;
    }

    public void setPositiveAmplitudeFastPolarityPulses(int positiveAmplitudeFastPolarityPulses) {
        this.positiveAmplitudeFastPolarityPulses = positiveAmplitudeFastPolarityPulses;
    }

    public int getLeakingTime() {
        return leakingTime;
    }

    public void setLeakingTime(int leakingTime) {
        this.leakingTime = leakingTime;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }

    public int getPositiveFastPolarityReversalTime() {
        return positiveFastPolarityReversalTime;
    }

    public void setPositiveFastPolarityReversalTime(int positiveFastPolarityReversalTime) {
        this.positiveFastPolarityReversalTime = positiveFastPolarityReversalTime;
    }

    public int getNegativeFastPolarityReversalTime() {
        return negativeFastPolarityReversalTime;
    }

    public void setNegativeFastPolarityReversalTime(int negativeFastPolarityReversalTime) {
        this.negativeFastPolarityReversalTime = negativeFastPolarityReversalTime;
    }

    public boolean isFirstPolarityReversal() {
        return firstPolarityReversal;
    }

    public void setFirstPolarityReversal(boolean firstPolarityReversal) {
        this.firstPolarityReversal = firstPolarityReversal;
    }

    public int getQuantityFastPolarityPulses() {
        return quantityFastPolarityPulses;
    }

    public void setQuantityFastPolarityPulses(int quantityFastPolarityPulses) {
        this.quantityFastPolarityPulses = quantityFastPolarityPulses;
    }

    public int getPositiveMeasureTime() {
        return positiveMeasureTime;
    }

    public void setPositiveMeasureTime(int positiveMeasureTime) {
        this.positiveMeasureTime = positiveMeasureTime;
    }

    public int getNegativeMeasureTime() {
        return negativeMeasureTime;
    }

    public void setNegativeMeasureTime(int negativeMeasureTime) {
        this.negativeMeasureTime = negativeMeasureTime;
    }

    public boolean isFirstPolarityMeasure() {
        return firstPolarityMeasure;
    }

    public void setFirstPolarityMeasure(boolean firstPolarityMeasure) {
        this.firstPolarityMeasure = firstPolarityMeasure;
    }

    public int getPositiveAmplitudeMeasurePulses() {
        return positiveAmplitudeMeasurePulses;
    }

    public void setPositiveAmplitudeMeasurePulses(int positiveAmplitudeMeasurePulses) {
        this.positiveAmplitudeMeasurePulses = positiveAmplitudeMeasurePulses;
    }

    public int getNegativeAmplitudeMeasurePulses() {
        return negativeAmplitudeMeasurePulses;
    }

    public void setNegativeAmplitudeMeasurePulses(int negativeAmplitudeMeasurePulses) {
        this.negativeAmplitudeMeasurePulses = negativeAmplitudeMeasurePulses;
    }
}
