package com.graph;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleAxesLineChart/* extends StackPane*/ {
    private final LineChart baseChart;
    private StackPane graphPane;
    private final ObservableList<LineChart> backgroundCharts = FXCollections.observableArrayList();
    private final Map<LineChart, Color> chartColorMap = new HashMap<>();

    private final double yAxisWidth = 60;
    private final AnchorPane detailsWindow;

    private final double yAxisSeparation = 20;
    private double strokeWidth = 0.3;

    public MultipleAxesLineChart(LineChart baseChart, Color lineColor) {
        this(baseChart, lineColor, null, null);
    }

    public MultipleAxesLineChart(LineChart baseChart, StackPane graphPane) {
        this(baseChart, Color.BLACK, null, graphPane);
    }

    public MultipleAxesLineChart(LineChart baseChart, Color lineColor, Double strokeWidth, StackPane graphPane) {
        if (strokeWidth != null) {
            this.strokeWidth = strokeWidth;
        }
        this.baseChart = baseChart;
        this.graphPane = graphPane;

        chartColorMap.put(baseChart, lineColor);

        styleBaseChart(baseChart);
//        styleChartLine(baseChart, lineColor);
        setFixedAxisWidth(baseChart);

        graphPane.setAlignment(Pos.CENTER_LEFT);

        backgroundCharts.addListener((Observable observable) -> rebuildChart());

        detailsWindow = new AnchorPane();
        bindMouseEvents(baseChart, this.strokeWidth);

        //setPlotTooltip(baseChart);

        rebuildChart();
    }

    private void bindMouseEvents(LineChart baseChart, Double strokeWidth) {
        final DetailsPopup detailsPopup = new DetailsPopup();
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
        yLine.setStrokeWidth(strokeWidth/2);
        xLine.setStrokeWidth(strokeWidth/2);
        xLine.setVisible(false);
        yLine.setVisible(false);

        final Node chartBackground = baseChart.lookup(".chart-plot-background");
        for (Node n: chartBackground.getParent().getChildrenUnmodifiable()) {
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
            xLine.setEndX(detailsWindow.getWidth()-10);
            xLine.setStartY(y+5);
            xLine.setEndY(y+5);

            yLine.setStartX(x+5);
            yLine.setEndX(x+5);
            yLine.setStartY(10);
            yLine.setEndY(detailsWindow.getHeight()-10);

            detailsPopup.showChartDescription(event);

            if (y + detailsPopup.getHeight() + 10 < graphPane.getHeight()) {
                AnchorPane.setTopAnchor(detailsPopup, y+10);
            } else {
                AnchorPane.setTopAnchor(detailsPopup, y-10-detailsPopup.getHeight());
            }

            if (x + detailsPopup.getWidth() + 10 < graphPane.getWidth()) {
                AnchorPane.setLeftAnchor(detailsPopup, x+10);
            } else {
                AnchorPane.setLeftAnchor(detailsPopup, x-10-detailsPopup.getWidth());
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

    private void rebuildChart() {
        graphPane.getChildren().clear();

        graphPane.getChildren().add(resizeBaseChart(baseChart));
        for (LineChart lineChart : backgroundCharts) {
            graphPane.getChildren().add(resizeBackgroundChart(lineChart));
        }
        graphPane.getChildren().add(detailsWindow);
    }

    private Node resizeBaseChart(LineChart lineChart) {
        HBox hBox = new HBox(lineChart);
        graphPane.getChildren().add(hBox);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.prefHeightProperty().bind(graphPane.heightProperty());
        hBox.prefWidthProperty().bind(graphPane.widthProperty());

        lineChart.minWidthProperty().bind(graphPane.widthProperty().subtract((yAxisWidth+yAxisSeparation)*backgroundCharts.size()));
        lineChart.prefWidthProperty().bind(graphPane.widthProperty().subtract((yAxisWidth+yAxisSeparation)*backgroundCharts.size()));
        lineChart.maxWidthProperty().bind(graphPane.widthProperty().subtract((yAxisWidth+yAxisSeparation)*backgroundCharts.size()));

        return lineChart;
    }

    private Node resizeBackgroundChart(LineChart lineChart) {
        HBox hBox = new HBox(lineChart);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.prefHeightProperty().bind(graphPane.heightProperty());
        hBox.prefWidthProperty().bind(graphPane.widthProperty());
        hBox.setMouseTransparent(true);

        lineChart.minWidthProperty().bind(graphPane.widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size()));
        lineChart.prefWidthProperty().bind(graphPane.widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size()));
        lineChart.maxWidthProperty().bind(graphPane.widthProperty().subtract((yAxisWidth + yAxisSeparation) * backgroundCharts.size()));

        lineChart.translateXProperty().bind(baseChart.getYAxis().widthProperty());
        lineChart.getYAxis().setTranslateX((yAxisWidth + yAxisSeparation) * backgroundCharts.indexOf(lineChart));

        return hBox;
    }

    public void addSeries(XYChart.Series series, Color lineColor) {
        NumberAxis yAxis = new NumberAxis();
        NumberAxis xAxis = new NumberAxis();

        // style x-axis
        xAxis.setAutoRanging(false);
        xAxis.setVisible(false);
        xAxis.setLabel("Время, мс");
        xAxis.setOpacity(0.0); // somehow the upper setVisible does not work
        xAxis.lowerBoundProperty().bind(((NumberAxis) baseChart.getXAxis()).lowerBoundProperty());
        xAxis.upperBoundProperty().bind(((NumberAxis) baseChart.getXAxis()).upperBoundProperty());
        xAxis.tickUnitProperty().bind(((NumberAxis) baseChart.getXAxis()).tickUnitProperty());

        // style y-axis
        yAxis.setSide(Side.RIGHT);
        yAxis.setLabel("Напряжение, мВ");

        // create chart
        LineChart lineChart = new LineChart(xAxis, yAxis);
        graphPane.getChildren().add(lineChart);

        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);
        //lineChart.setHorizontalGridLinesVisible(false);
        lineChart.getData().add(series);
        setPlotTooltip(lineChart);

        styleBackgroundChart(lineChart, lineColor);
        setFixedAxisWidth(lineChart);

        chartColorMap.put(lineChart, lineColor);
        backgroundCharts.add(lineChart);
    }

    private void setPlotTooltip(LineChart lineChart) {
//        Node chartSeries = visualPlot.lookup(".chart-series-line");
//        chartSeries.setOnMouseMoved(event -> {
//            System.out.println("hi");
//            coordinateLabel.setText(
//                    String.format(
//                            "(%.2f, %.2f)",
//                            visualPlot.getXAxis().getValueForDisplay(event.getX()),
//                            visualPlot.getYAxis().getValueForDisplay(event.getY())
//                    )
//            );
//        });
        ObservableList<XYChart.Data> dataList = ((XYChart.Series) lineChart.getData().get(0)).getData();
        dataList.forEach(data -> {
            Node node = data.getNode();
            Tooltip tooltip = new Tooltip("Время = " + data.getXValue().toString() + ", мс\nАмплитуда = " + data.getYValue().toString() + ", мВ");
            Tooltip.install(node, tooltip);
        });
    }

    private void styleBackgroundChart(LineChart lineChart, Color lineColor) {
        styleChartLine(lineChart, lineColor);

//        Node contentBackground = lineChart.lookup(".chart-content").lookup(".chart-plot-background");
//        contentBackground.setStyle("-fx-background-color: transparent;");

        lineChart.setVerticalZeroLineVisible(false);
        lineChart.setHorizontalZeroLineVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setHorizontalGridLinesVisible(false);
//        lineChart.setCreateSymbols(false);
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void styleChartLine(LineChart chart, Color lineColor) {
        chart.getYAxis().lookup(".axis-label").setStyle("-fx-text-fill: " + toRGBCode(lineColor) + "; -fx-font-weight: bold;");
        Node seriesLine = chart.lookup(".chart-series-line");
        seriesLine.setStyle("-fx-stroke: " + toRGBCode(lineColor) + "; -fx-stroke-width: " + 2.0 + ";");
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
       // hBox.getChildren().add(baseChartCheckBox);

        for (final LineChart lineChart : backgroundCharts) {
            CheckBox checkBox = new CheckBox(lineChart.getYAxis().getLabel());
            checkBox.setStyle("-fx-text-fill: " + toRGBCode(chartColorMap.get(lineChart)) + "; -fx-font-weight: bold");
            checkBox.setSelected(true);
            checkBox.setOnAction(event -> {
                if (backgroundCharts.contains(lineChart)) {
                    backgroundCharts.remove(lineChart);
                } else {
                    backgroundCharts.add(lineChart);
                }
            });
            hBox.getChildren().add(checkBox);
        }

        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);
        hBox.setStyle("-fx-padding: 0 10 20 10");

        return hBox;
    }

    private class DetailsPopup extends VBox {

        private DetailsPopup() {
            setStyle("-fx-border-width: 1px; -fx-padding: 5 5 5 5px; -fx-border-color: gray; -fx-background-color: whitesmoke;");
            setVisible(false);
        }

        public void showChartDescription(MouseEvent event) {
            getChildren().clear();

            Long xValueLong = Math.round((double)baseChart.getXAxis().getValueForDisplay(event.getX()));

            HBox baseChartPopupRow = buildPopupRow(event, xValueLong, baseChart);
            if (baseChartPopupRow != null) {
                getChildren().add(baseChartPopupRow);
            }

            for (LineChart lineChart : backgroundCharts) {
                HBox popupRow = buildPopupRow(event, xValueLong, lineChart);
                if (popupRow == null) continue;

                getChildren().add(popupRow);
            }
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
            if(series.getName().contains("Напряжение")){
                popupRow = new HBox(10, seriesName, new Label("амплитуда = "+amplitude+" мВ\nвремя = "+timeRounded+" мс"));
            } else{
                popupRow = new HBox(10, seriesName, new Label("амплитуда = "+amplitude+" мкА\nвремя = "+timeRounded+" мс"));
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
            List<XYChart.Data> dataList = ((List<XYChart.Data>)((XYChart.Series)chart.getData().get(0)).getData());
            for (XYChart.Data data : dataList) {
                if (data.getXValue().equals(xValue)) {
                    return (Number)data.getYValue();
                }
            }
            return null;
        }
    }

}
