package com.entity;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable {
    private String userName = "NoName";
    private int idHigh = 0;
    private int idLow = 0;
    private int stripType;
    private List<Number> currentXMeasurement;
    private List<Number> voltageYMeasurement;
    private List<Number> currentYMeasurement;
    private String comment = "Комментарии отсутствуют";

    //private MeasurementSetup measurementSetup;

    public Data() {
        currentXMeasurement = new ArrayList<>();
        currentYMeasurement = new ArrayList<>();
        voltageYMeasurement = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIdHigh() {
        return idHigh;
    }

    public void setIdHigh(int idHigh) {
        this.idHigh = idHigh;
    }

    public int getIdLow() {
        return idLow;
    }

    public void setIdLow(int idLow) {
        this.idLow = idLow;
    }

    public int getStripType() {
        return stripType;
    }

    public void setStripType(int stripType) {
        this.stripType = stripType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Number> getCurrentXMeasurement() {
        return currentXMeasurement;
    }

    public void setCurrentXMeasurement(List<Number> currentXMeasurement) {
        this.currentXMeasurement = currentXMeasurement;
    }

    public List<Number> getCurrentYMeasurement() {
        return currentYMeasurement;
    }

    public void setCurrentYMeasurement(List<Number> currentYMeasurement) {
        this.currentYMeasurement = currentYMeasurement;
    }

    public List<Number> getVoltageYMeasurement() {
        return voltageYMeasurement;
    }

    public void setVoltageYMeasurement(List<Number> voltageYMeasurement) {
        this.voltageYMeasurement = voltageYMeasurement;
    }

    @Override
    public String toString() {
        return "Data{" +
                "userName='" + userName + '\'' +
                ", idHigh=" + idHigh +
                ", idLow=" + idLow +
                ", stripType=" + stripType +
                ", currentXMeasurement=" + currentXMeasurement +
                ", voltageYMeasurement=" + voltageYMeasurement +
                ", currentYMeasurement=" + currentYMeasurement +
                ", comment='" + comment + '\'' +
                '}';
    }
}
