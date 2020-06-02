package com.graph;

import com.entity.Data;
import com.entity.MeasureMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleSameAxesLineChart {

    final static Logger logger = Logger.getLogger(MultipleSameAxesLineChart.class);

    private final LineChart baseChart;
    private StackPane graphPane;
    private final ObservableList<XYChart.Series> backgroundCharts = FXCollections.observableArrayList();
    private final Map<XYChart.Series, Color> chartColorMap = new HashMap<>();
    private final Map<XYChart.Series, MeasureMode> chartDataMap = new HashMap<>();

    private final double yAxisWidth = 60;
    private final AnchorPane detailsWindow;

    private final double yAxisSeparation = 20;
    private double strokeWidth = 0.3;

    private Number xValue = 0;
    private Number yMaxValue = 0;
    private Number yMinValue = 0;

    public MultipleSameAxesLineChart(LineChart baseChart, StackPane graphPane) {
        this(baseChart, Color.BLACK, null, graphPane);

    }

    public MultipleSameAxesLineChart(LineChart baseChart, Color lineColor, Double strokeWidth, StackPane graphPane) {
        if (strokeWidth != null) {
            this.strokeWidth = strokeWidth;
        }
        this.baseChart = baseChart;
        this.graphPane = graphPane;

        //chartColorMap.put((XYChart.Series) baseChart.getData().get(0), lineColor);

        styleBaseChart(baseChart);
//        styleChartLine(baseChart, lineColor);
        setFixedAxisWidth(baseChart);

        graphPane.setAlignment(Pos.CENTER_LEFT);

        // backgroundCharts.addListener((Observable observable) -> rebuildChart());

        detailsWindow = new AnchorPane();
        bindMouseEvents(baseChart, this.strokeWidth);

    }

    private void bindMouseEvents(LineChart baseChart, Double strokeWidth) {
        final MultipleSameAxesLineChart.DetailsPopup detailsPopup = new MultipleSameAxesLineChart.DetailsPopup();
        graphPane.getChildren().add(detailsWindow);
        detailsWindow.getChildren().add(detailsPopup);
        detailsWindow.prefHeightProperty().bind(graphPane.heightProperty());
        detailsWindow.prefWidthProperty().bind(graphPane.widthProperty());
        detailsWindow.setMouseTransparent(true);

        graphPane.setOnMouseMoved(null);
        graphPane.setMouseTransparent(false);

        final Axis xAxis = baseChart.getXAxis();
        final Axis yAxis = baseChart.getYAxis();

        final Line xLine = new Line();
        final Line yLine = new Line();
        yLine.setFill(Color.GRAY);
        xLine.setFill(Color.GRAY);
        yLine.setStrokeWidth(strokeWidth / 2);
        xLine.setStrokeWidth(strokeWidth / 2);
        xLine.setVisible(false);
        yLine.setVisible(false);

        final Node chartBackground = baseChart.lookup(".chart-plot-background");
        for (Node n : chartBackground.getParent().getChildrenUnmodifiable()) {
            if (n != chartBackground && n != xAxis && n != yAxis) {
                n.setMouseTransparent(true);
            }
        }
        chartBackground.setCursor(Cursor.CROSSHAIR);
        chartBackground.setOnMouseEntered((event) -> {
            chartBackground.getOnMouseMoved().handle(event);
            detailsPopup.setVisible(true);
            xLine.setVisible(true);
            yLine.setVisible(true);
            detailsWindow.getChildren().addAll(xLine, yLine);
        });
        chartBackground.setOnMouseExited((event) -> {
            detailsPopup.setVisible(false);
            xLine.setVisible(false);
            yLine.setVisible(false);
            detailsWindow.getChildren().removeAll(xLine, yLine);
        });
        chartBackground.setOnMouseMoved(event -> {
            double x = event.getX() + chartBackground.getLayoutX();
            double y = event.getY() + chartBackground.getLayoutY();

            xLine.setStartX(10);
            xLine.setEndX(detailsWindow.getWidth() - 10);
            xLine.setStartY(y + 5);
            xLine.setEndY(y + 5);

            yLine.setStartX(x + 5);
            yLine.setEndX(x + 5);
            yLine.setStartY(10);
            yLine.setEndY(detailsWindow.getHeight() - 10);

            detailsPopup.showChartDescription(event);

            if (y + detailsPopup.getHeight() + 10 < graphPane.getHeight()) {
                AnchorPane.setTopAnchor(detailsPopup, y + 10);
            } else {
                AnchorPane.setTopAnchor(detailsPopup, y - 10 - detailsPopup.getHeight());
            }

            if (x + detailsPopup.getWidth() + 10 < graphPane.getWidth()) {
                AnchorPane.setLeftAnchor(detailsPopup, x + 10);
            } else {
                AnchorPane.setLeftAnchor(detailsPopup, x - 10 - detailsPopup.getWidth());
            }
        });
    }

    private void styleBaseChart(LineChart baseChart) {
        //baseChart.setCreateSymbols(false);
        baseChart.setLegendVisible(false);
        //baseChart.getXAxis().setAutoRanging(false);
        baseChart.getXAxis().setAnimated(false);
        baseChart.getYAxis().setAnimated(false);
    }

    private void setFixedAxisWidth(LineChart chart) {
        chart.getYAxis().setPrefWidth(yAxisWidth);
        chart.getYAxis().setMaxWidth(yAxisWidth);
    }

    public void addSeries(XYChart.Series series, Color lineColor, String seriesName, MeasureMode mode) {
        baseChart.getData().add(series);

        styleBackgroundChart(series, lineColor);
        //setFixedAxisWidth(baseChart);
        chartDataMap.put(series, mode);
        chartColorMap.put(series, lineColor);
        backgroundCharts.add(series);

    }

    private void styleBackgroundChart(XYChart.Series series, Color lineColor) {
        styleChartLine(series, lineColor);

//        Node contentBackground = lineChart.lookup(".chart-content").lookup(".chart-plot-background");
//        contentBackground.setStyle("-fx-background-color: transparent;");

//        lineChart.setCreateSymbols(false);
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public XYChart.Series styleChartLine(XYChart.Series series, Color lineColor) {
        Node seriesLine = series.getNode().lookup(".chart-series-line");
        seriesLine.setStyle("-fx-stroke: " + toRGBCode(lineColor) + "; -fx-stroke-width: " + 2.0 + ";");
        return series;
    }

    public Node getLegend() {
        HBox hBox = new HBox();

        //final CheckBox baseChartCheckBox = new CheckBox(baseChart.getYAxis().getLabel());
        final CheckBox baseChartCheckBox = new CheckBox();
        XYChart.Series series = (XYChart.Series) baseChart.getData().get(0);
        baseChartCheckBox.setText(series.getName());
        baseChartCheckBox.setSelected(true);
        baseChartCheckBox.setStyle("-fx-text-fill: " + toRGBCode(chartColorMap.get(baseChart)) + "; -fx-font-weight: bold;");
        baseChartCheckBox.setDisable(true);
        baseChartCheckBox.getStyleClass().add("readonly-checkbox");
        baseChartCheckBox.setOnAction(event -> baseChartCheckBox.setSelected(true));
        hBox.getChildren().add(baseChartCheckBox);

        for (final XYChart.Series series1 : backgroundCharts) {
            CheckBox checkBox = new CheckBox(series1.getName());
            checkBox.setStyle("-fx-text-fill: " + toRGBCode(chartColorMap.get(series1)) + "; -fx-font-weight: bold");
            checkBox.setSelected(true);
            checkBox.setOnAction(event -> {
                if (backgroundCharts.contains(series1)) {
                    backgroundCharts.remove(series1);
                } else {
                    backgroundCharts.add(series1);
                }
            });
            hBox.getChildren().add(checkBox);
        }

        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.setStyle("-fx-padding: 0 10 20 10");

        return hBox;
    }

    public Color getSeriesColor(XYChart.Series series){
        return chartColorMap.get(series);
    }

    public void setColor(XYChart.Series series, Color color){
        chartColorMap.put(series, color);
    }

    public MeasureMode getChartData(XYChart.Series series){
        return chartDataMap.get(series);
    }

    private class DetailsPopup extends VBox {

        private DetailsPopup() {
            setStyle("-fx-border-width: 1px; -fx-padding: 5 5 5 5px; -fx-border-color: gray; -fx-background-color: whitesmoke;");
            setVisible(false);
        }

        public void showChartDescription(MouseEvent event) {
            getChildren().clear();

            Long xValueLong = Math.round((double) baseChart.getXAxis().getValueForDisplay(event.getX()));

            HBox baseChartPopupRow = buildPopupRow(event, xValueLong, baseChart);
            if (baseChartPopupRow != null) {
                getChildren().add(baseChartPopupRow);
            }

            //for (XYChart.Series series : backgroundCharts) {
            HBox popupRow = buildPopupRow(event, xValueLong, baseChart);
            //   if (popupRow == null) continue;

            //getChildren().add(popupRow);
            //}
        }

        private HBox buildPopupRow(MouseEvent event, Long xValueLong, LineChart lineChart) {
            Label seriesName = new Label(lineChart.getYAxis().getLabel());
            XYChart.Series series = (XYChart.Series) lineChart.getData().get(0);
            seriesName.setText(series.getName());
            seriesName.setTextFill(chartColorMap.get(lineChart));

            Number yValueForChart = getYValueForX(lineChart, xValueLong.intValue());
            if (yValueForChart == null) {
                //return null;
            }
            Number yValueLower = Math.round(normalizeYValue(lineChart, event.getY() - 10));
            Number yValueUpper = Math.round(normalizeYValue(lineChart, event.getY() + 10));
            Number yValueUnderMouse = Math.round((double) lineChart.getYAxis().getValueForDisplay(event.getY()));

            // make series name bold when mouse is near given chart's line
//            if (isMouseNearLine(yValueForChart, yValueUnderMouse, Math.abs(yValueLower.doubleValue()-yValueUpper.doubleValue()))) {
//                seriesName.setStyle("-fx-font-weight: bold");
//            }

            Number timeRounded = Math.round((Double) lineChart.getXAxis().getValueForDisplay(event.getX()));
            Number time = (Double) lineChart.getXAxis().getValueForDisplay(event.getX());
            Number amplitudeN = (Double) lineChart.getYAxis().getValueForDisplay(event.getY());
            String amplitude = String.format("%.2f", amplitudeN);
            Number amplitudeRounded = Math.round((Double) lineChart.getYAxis().getValueForDisplay(event.getY()));
            HBox popupRow;
            if(series.getName().contains(".gl")) {
                popupRow = new HBox(new Label("амплитуда = " + amplitude + " мкА\nвремя = " + timeRounded + " мс"));
            } else {
                popupRow = new HBox(new Label("ток = " + amplitude + " мкА\nнапряжение = " + timeRounded + " мВ"));
            }

            return popupRow;
        }

        private double normalizeYValue(LineChart lineChart, double value) {
            Double val = (Double) lineChart.getYAxis().getValueForDisplay(value);
            if (val == null) {
                return 0;
            } else {
                return val;
            }
        }

        private boolean isMouseNearLine(Number realYValue, Number yValueUnderMouse, Double tolerance) {
            return (Math.abs(yValueUnderMouse.doubleValue() - realYValue.doubleValue()) < tolerance);
        }

        public Number getYValueForX(LineChart chart, Number xValue) {
            List<XYChart.Data> dataList = ((List<XYChart.Data>) ((XYChart.Series) chart.getData().get(0)).getData());
            for (XYChart.Data data : dataList) {
                if (data.getXValue().equals(xValue)) {
                    return (Number) data.getYValue();
                }
            }
            return null;
        }
    }

}
