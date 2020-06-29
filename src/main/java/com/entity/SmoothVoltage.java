package com.entity;

import com.comPort.Control;

public class SmoothVoltage {

    private int positiveTime = 100;
    private int negativeTime = 100;
    private int positiveBegin = 0;
    private int positiveEnd = 0;
    private int negativeBegin = 0;
    private int negativeEnd = 0;
    private boolean isSetup = false;

    public SmoothVoltage() {

    }

    public SmoothVoltage(int positiveTime, int negativeTime, int positiveBegin, int positiveEnd, int negativeBegin, int negativeEnd, boolean isSetup) {
        this.positiveTime = positiveTime;
        this.negativeTime = negativeTime;
        this.positiveBegin = positiveBegin;
        this.positiveEnd = positiveEnd;
        this.negativeBegin = negativeBegin;
        this.negativeEnd = negativeEnd;
        this.isSetup = isSetup;
    }

    public int getPositiveTime() {
        return positiveTime;
    }

    public void setPositiveTime(int positiveTime) {
        this.positiveTime = positiveTime;
    }

    public int getNegativeTime() {
        return negativeTime;
    }

    public void setNegativeTime(int negativeTime) {
        this.negativeTime = negativeTime;
    }

    public int getPositiveBegin() {
        return positiveBegin;
    }

    public void setPositiveBegin(int positiveBegin) {
        this.positiveBegin = positiveBegin;
    }

    public int getPositiveEnd() {
        return positiveEnd;
    }

    public void setPositiveEnd(int positiveEnd) {
        this.positiveEnd = positiveEnd;
    }

    public int getNegativeBegin() {
        return negativeBegin;
    }

    public void setNegativeBegin(int negativeBegin) {
        this.negativeBegin = negativeBegin;
    }

    public int getNegativeEnd() {
        return negativeEnd;
    }

    public void setNegativeEnd(int negativeEnd) {
        this.negativeEnd = negativeEnd;
    }

    public boolean isSetup() {
        return isSetup;
    }

    public void setSetup(boolean setup) {
        isSetup = setup;
    }

    public byte[] getTransmitArray() {
        byte[] transmitBytes = new byte[23];
        transmitBytes[0] = Control.SMOOTH_CMD;
        transmitBytes[1] = isSetup ? (byte) 1 : 0;
        transmitBytes[4] = (byte) positiveTime;
        transmitBytes[3] = (byte) (positiveTime >> 8);
        transmitBytes[2] = (byte) (positiveTime >> 16);
        transmitBytes[7] = (byte) negativeTime;
        transmitBytes[6] = (byte) (negativeTime >> 8);
        transmitBytes[5] = (byte) (negativeTime >> 16);
        transmitBytes[8] = Control.END_CMD;
        transmitBytes[9] = positiveBegin > 0 ? (byte) 0 : 1;
        transmitBytes[11] = (byte) Math.abs(positiveBegin);
        transmitBytes[10] = (byte) Math.abs((positiveBegin >> 8));
        transmitBytes[12] = positiveEnd > 0 ? (byte) 0 : 1;
        transmitBytes[14] = (byte) Math.abs(positiveEnd);
        transmitBytes[13] = (byte) Math.abs((positiveEnd >> 8));
        transmitBytes[15] = negativeBegin > 0 ? (byte) 0 : 1;
        transmitBytes[17] = (byte) Math.abs(negativeBegin);
        transmitBytes[16] = (byte) Math.abs((negativeBegin >> 8));
        transmitBytes[18] = negativeEnd > 0 ? (byte) 0 : 1;
        transmitBytes[20] = (byte) Math.abs(negativeEnd);
        transmitBytes[19] = (byte) Math.abs((negativeEnd >> 8));
        transmitBytes[21] = (byte) 21;
        transmitBytes[22] = Control.SMOOTH_CMD;
        return transmitBytes;
    }

    @Override
    public String toString() {
        return "SmoothVoltage{" +
                "positiveTime=" + positiveTime +
                ", negativeTime=" + negativeTime +
                ", positiveBegin=" + positiveBegin +
                ", positiveEnd=" + positiveEnd +
                ", negativeBegin=" + negativeBegin +
                ", negativeEnd=" + negativeEnd +
                ", isSetup=" + isSetup +
                '}';
    }
}
