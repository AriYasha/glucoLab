package com.entity;

import com.comPort.Control;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Arrays;

public class PolySetup implements Serializable, MeasureMode {

    final private static Logger logger = Logger.getLogger(PolySetup.class);

    private int beginPoint;
    private int mediumPoint;
    private int lastPoint;
    private int quantityReapeted;
    private int increaseTime;
    private int decreaseTime;
    private int discrDACmV;
    private int nullDiscrDAC;

    private Data data;

    public PolySetup() {
    }

    public int getBeginPoint() {
        return beginPoint;
    }

    public void setBeginPoint(int beginPoint) {
        this.beginPoint = beginPoint;
    }

    public int getMediumPoint() {
        return mediumPoint;
    }

    public void setMediumPoint(int mediumPoint) {
        this.mediumPoint = mediumPoint;
    }

    public int getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(int lastPoint) {
        this.lastPoint = lastPoint;
    }

    public int getQuantityReapeted() {
        return quantityReapeted;
    }

    public void setQuantityReapeted(int quantityReapeted) {
        this.quantityReapeted = quantityReapeted;
    }

    public int getIncreaseTime() {
        return increaseTime;
    }

    public void setIncreaseTime(int increaseTime) {
        this.increaseTime = increaseTime;
    }

    public int getDecreaseTime() {
        return decreaseTime;
    }

    public void setDecreaseTime(int decreaseTime) {
        this.decreaseTime = decreaseTime;
    }

    public int getDiscrDACmV() {
        return discrDACmV;
    }

    public void setDiscrDACmV(int discrDACmV) {
        this.discrDACmV = discrDACmV;
    }

    public int getNullDiscrDAC() {
        return nullDiscrDAC;
    }

    public void setNullDiscrDAC(int nullDiscrDAC) {
        this.nullDiscrDAC = nullDiscrDAC;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public byte[] getTransmitArray(){
        byte[] transmitBytes = new byte[26];
        transmitBytes[0] = Control.POLY_SETUP_CMD;
        if(beginPoint >= 0){
            transmitBytes[1] = (byte) 0;
            transmitBytes[3] = (byte) beginPoint;
            transmitBytes[2] = (byte) (beginPoint >> 8);
        } else {
            transmitBytes[1] = (byte) 1;
            transmitBytes[3] = (byte) Math.abs(beginPoint);
            transmitBytes[2] = (byte) (Math.abs(beginPoint) >> 8);
        }
        if(mediumPoint >= 0){
            transmitBytes[4] = (byte) 0;
            transmitBytes[6] = (byte) mediumPoint;
            transmitBytes[5] = (byte) (mediumPoint >> 8);
        } else {
            transmitBytes[4] = (byte) 1;
            transmitBytes[6] = (byte) Math.abs(mediumPoint);
            transmitBytes[5] = (byte) (Math.abs(mediumPoint) >> 8);
        }
        if(lastPoint >= 0){
            transmitBytes[7] = (byte) 0;
            transmitBytes[9] = (byte) lastPoint;
            transmitBytes[8] = (byte) (lastPoint >> 8);
        } else {
            transmitBytes[7] = (byte) 1;
            transmitBytes[9] = (byte) Math.abs(lastPoint);
            transmitBytes[8] = (byte) (Math.abs(lastPoint) >> 8);
        }
        transmitBytes[10] = (byte) 170;
        transmitBytes[13] = (byte) increaseTime;
        transmitBytes[12] = (byte) (increaseTime >> 8);
        transmitBytes[11] = (byte) (increaseTime >> 16);
        transmitBytes[16] = (byte) decreaseTime;
        transmitBytes[15] = (byte) (decreaseTime >> 8);
        transmitBytes[14] = (byte) (decreaseTime >> 16);
        transmitBytes[17] = (byte) 170;
        transmitBytes[19] = (byte) quantityReapeted;
        transmitBytes[18] = (byte) (quantityReapeted >> 8);
        transmitBytes[21] = (byte) discrDACmV;
        transmitBytes[20] = (byte) (discrDACmV >> 8);
        transmitBytes[23] = (byte) nullDiscrDAC;
        transmitBytes[22] = (byte) (nullDiscrDAC >> 8);
        transmitBytes[24] = 0x18;
        transmitBytes[25] = Control.POLY_SETUP_CMD;
        logger.debug(Arrays.toString(transmitBytes));
        return transmitBytes;
    }

    @Override
    public String toString() {
        return "Описание выбранного графика :\n" +
                "\nТип полоски :\n\t" + data.getStripType() +
                "\nКомментарий :\n\t" + data.getComment() +
                "\nАмплитуда начальной точки :\n\t" + getBeginPoint() + ", мВ" +
                "\nАмплитуда средней точки :\n\t" + getMediumPoint() + ", мВ" +
                "\nАмплитуда конечной точки :\n\t" + getLastPoint() + ", мВ" +
                "\nВремя нарастания :\n\t" + getIncreaseTime() + ", мс" +
                "\nВремя спада :\n\t" + getDecreaseTime() + ", мс" +
                "\nКоличество повторений :\n\t" + getQuantityReapeted() +
                "";
    }
}
