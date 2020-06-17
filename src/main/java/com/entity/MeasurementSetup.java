package com.entity;

import com.comPort.Control;

import java.io.Serializable;

public class MeasurementSetup implements Serializable, MeasureMode {
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

    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public byte[] getTransmitArray(){
        byte[] transmitBytes = new byte[30];
        transmitBytes[0] = Control.SETUP_CMD;
        transmitBytes[1] = (byte) 1;
        transmitBytes[3] = (byte) leakingTime;
        transmitBytes[2] = (byte) (leakingTime >> 8);
        transmitBytes[5] = (byte) pauseTime;
        transmitBytes[4] = (byte) (pauseTime >> 8);
        transmitBytes[6] = (byte) 170;
        transmitBytes[8] = (byte) positiveFastPolarityReversalTime;
        transmitBytes[7] = (byte) (positiveFastPolarityReversalTime >> 8);
        transmitBytes[10] = (byte) negativeFastPolarityReversalTime;
        transmitBytes[9] = (byte) (negativeFastPolarityReversalTime >> 8);
        transmitBytes[11] = (byte) (firstPolarityReversal ? 0 : 1);
        transmitBytes[13] = (byte) positiveAmplitudeFastPolarityPulses;
        transmitBytes[12] = (byte) (positiveAmplitudeFastPolarityPulses >> 8);
        transmitBytes[15] = (byte) negativeAmplitudeFastPolarityPulses;
        transmitBytes[14] = (byte) (negativeAmplitudeFastPolarityPulses >> 8);
        transmitBytes[17] = (byte) quantityFastPolarityPulses;
        transmitBytes[16] = (byte) (quantityFastPolarityPulses >> 8);
        transmitBytes[18] = (byte) 170;
        transmitBytes[20] = (byte) positiveMeasureTime;
        transmitBytes[19] = (byte) (positiveMeasureTime >> 8);
        transmitBytes[22] = (byte) negativeMeasureTime;
        transmitBytes[21] = (byte) (negativeMeasureTime >> 8);
        transmitBytes[23] = (byte) (firstPolarityMeasure ? 0 : 1);
        transmitBytes[25] = (byte) positiveAmplitudeMeasurePulses;
        transmitBytes[24] = (byte) (positiveAmplitudeMeasurePulses >> 8);
        transmitBytes[27] = (byte) negativeAmplitudeMeasurePulses;
        transmitBytes[26] = (byte) (negativeAmplitudeMeasurePulses >> 8);
        transmitBytes[28] = (byte) 28;
        transmitBytes[29] = Control.SETUP_CMD;
        return transmitBytes;
    }

    @Override
    public String toString() {
        return "Описание выбранного графика :\n" +
                "\nТип полоски :\n\t" + data.getStripType() +
                "\nКомментарий :\n\t" + data.getComment() +
                "\nВремя протекания :\n\t" + getLeakingTime() + ", мс" +
                "\nВремя паузы :\n\t" + getPauseTime() + ", мс" +
                "\nКоличество импульсов ПП :\n\t" + getQuantityFastPolarityPulses() +
                "\nАмплитуда положительных импульсов ПП :\n\t" + getPositiveAmplitudeFastPolarityPulses() + ", мВ" +
                "\nАмплитуда отрицательных импульсов ПП :\n\t" + getNegativeAmplitudeFastPolarityPulses() +
                "\nВремя отрицательных импульсов ПП :\n\t" + getNegativeFastPolarityReversalTime() + ", мс" +
                "\nВремя положительных импульсов ПП :\n\t" + getPositiveFastPolarityReversalTime() + ", мс" +
                "\nАмплитуда положительного импульса измерения :\n\t" + getNegativeAmplitudeMeasurePulses() + ", мВ" +
                "\nАмплитуда отрицательного импульса измерения :\n\t" + getPositiveAmplitudeMeasurePulses() + ", мВ" +
                "\nВремя отрицательного импульса измерения :\n\t" + getNegativeMeasureTime() + ", мс" +
                "\nВремя положительного импульса измерения :\n\t" + getPositiveMeasureTime() + ", мс" +
                "";
    }
}
