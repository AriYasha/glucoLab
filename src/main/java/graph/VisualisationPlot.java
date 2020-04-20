package graph;

import entity.MeasurementSetup;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

public class VisualisationPlot {
    private LineChart<Number, Number> visualPlot;

    public VisualisationPlot(LineChart<Number, Number> visualPlot) {
        this.visualPlot = visualPlot;
    }

    public void drawGraphic(MeasurementSetup setup) {
        visualPlot.getData().clear();
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


        visualPlot.getData().add(series);
        series.getNode().setOnMouseClicked(e -> System.out.println("Click on series"));
        ObservableList<XYChart.Data> dataList = ((XYChart.Series) visualPlot.getData().get(0)).getData();
        dataList.forEach(data->{
            Node node = data.getNode();
            Tooltip tooltip = new Tooltip('('+data.getXValue().toString()+';'+data.getYValue().toString()+')');
            Tooltip.install(node, tooltip);
        });



//        for (int x = 0 ; x <= 20 ; x++) {
//
//            // handler for clicking on data point:
//            data.getNode().setOnMouseClicked(e ->
//                    System.out.printf("Click on data [%d, %d]%n", data.getXValue(), data.getYValue()));
//        }


    }
}
