package com.entity;

import java.io.Serializable;

public class PolySetup implements Serializable {
    private int beginPoint;
    private int mediumPoint;
    private int lastPoint;
    private int quantityReapeted;
    private int increaseTime;
    private int decreaseTime;

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
}
