package com.graph;

import com.entity.Data;
import com.entity.MeasurementSetup;
import com.entity.PolySetup;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class VisualisationPlot {
    private LineChart<Number, Number> visualPlot;

    public VisualisationPlot(LineChart<Number, Number> visualPlot) {
        this.visualPlot = visualPlot;
    }

    public LineChart drawMeasureGraphic(LineChart visualPlot, MeasurementSetup setup) {
        //visualPlot.getData().clear();
        int time = 0;
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        visualPlot.setLegendVisible(false);
        series.getData().add(new XYChart.Data<>(time, 0));
        series.getData().add(new XYChart.Data<>(time += setup.getLeakingTime(), 0));
        series.getData().add(new XYChart.Data<>(time += setup.getPauseTime(), 0));
        if (setup.isFirstPolarityReversal()) {
            for (int count = 0; count < setup.getQuantityFastPolarityPulses(); count++) {
                series.getData().add(new XYChart.Data<>(time, setup.getPositiveAmplitudeFastPolarityPulses()));
                series.getData().add(new XYChart.Data<>(time += setup.getPositiveFastPolarityReversalTime(), setup.getPositiveAmplitudeFastPolarityPulses()));
                series.getData().add(new XYChart.Data<>(time, 0 - setup.getNegativeAmplitudeFastPolarityPulses()));
                series.getData().add(new XYChart.Data<>(time += setup.getNegativeFastPolarityReversalTime(), 0 - setup.getNegativeAmplitudeFastPolarityPulses()));
            }
        } else {
            for (int count = 0; count < setup.getQuantityFastPolarityPulses(); count++) {
                series.getData().add(new XYChart.Data<>(time, 0 - setup.getNegativeAmplitudeFastPolarityPulses()));
                series.getData().add(new XYChart.Data<>(time += setup.getNegativeFastPolarityReversalTime(), 0 - setup.getNegativeAmplitudeFastPolarityPulses()));
                series.getData().add(new XYChart.Data<>(time, setup.getPositiveAmplitudeFastPolarityPulses()));
                series.getData().add(new XYChart.Data<>(time += setup.getPositiveFastPolarityReversalTime(), setup.getPositiveAmplitudeFastPolarityPulses()));
            }
        }
        if (setup.isFirstPolarityMeasure()) {
            series.getData().add(new XYChart.Data<>(time, setup.getPositiveAmplitudeMeasurePulses()));
            series.getData().add(new XYChart.Data<>(time += setup.getPositiveMeasureTime(), setup.getPositiveAmplitudeMeasurePulses()));
            series.getData().add(new XYChart.Data<>(time, 0 - setup.getNegativeAmplitudeMeasurePulses()));
            series.getData().add(new XYChart.Data<>(time += setup.getNegativeMeasureTime(), 0 - setup.getNegativeAmplitudeMeasurePulses()));
        } else {
            series.getData().add(new XYChart.Data<>(time, 0 - setup.getNegativeAmplitudeMeasurePulses()));
            series.getData().add(new XYChart.Data<>(time += setup.getNegativeMeasureTime(), 0 - setup.getNegativeAmplitudeMeasurePulses()));
            series.getData().add(new XYChart.Data<>(time, setup.getPositiveAmplitudeMeasurePulses()));
            series.getData().add(new XYChart.Data<>(time += setup.getPositiveMeasureTime(), setup.getPositiveAmplitudeMeasurePulses()));
        }
        series.getData().add(new XYChart.Data<>(time, 0));

        visualPlot.getData().clear();
        visualPlot.setAnimated(false);
        visualPlot.getData().add(series);

//        Node chartSeries = visualPlot.lookup(".chart-series-line");
//        System.out.println(chartSeries.toString());
//        chartSeries.setOnMouseMoved(event -> {
//            System.out.println("hi");
//        });

        return visualPlot;

    }

    public LineChart drawPolyGraphic(LineChart visualPlot, PolySetup setup) {
        int time = 0;
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        visualPlot.setLegendVisible(false);
        for(int i = 1; i <= setup.getQuantityReapeted(); i++) {
            series.getData().add(new XYChart.Data<>(time, setup.getBeginPoint()));
            series.getData().add(new XYChart.Data<>(time += setup.getIncreaseTime(), setup.getMediumPoint()));
            series.getData().add(new XYChart.Data<>(time += setup.getDecreaseTime(), setup.getLastPoint()));
        }
        visualPlot.getData().clear();
        visualPlot.getData().add(series);
        return visualPlot;
    }

    public static XYChart.Series<Number, Number> prepareSeries(String name, Data data) {
        ArrayList<Number> xValues = (ArrayList<Number>) data.getCurrentXMeasurement();
        ArrayList<Number> yValues = (ArrayList<Number>) data.getCurrentYMeasurement();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (int i = 0; i < xValues.size(); i++) {
            series.getData().add(new XYChart.Data<>(xValues.get(i), yValues.get(i)));
        }
        return series;
    }
}
