package utils;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;

/* CustomSeries.java
 *
 * Copyright (C) 2017 Kevin Lopez Andrade <kevin@kevlopez.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Util class to add and delete in runtime series from a XYChart
 */

public class CustomSeries {

    private XYChart.Series<String,Number> series;
    private XYChart.Series<String,Number> lineSeries;
    private StackedBarChart<String, Number> stackedBarChart;
    private LineChart<String, Number> lineChart;

    public CustomSeries (StackedBarChart<String,Number> stackedBarChart, LineChart<String,Number> line) {
        this.series =  new XYChart.Series<>();
        this.lineSeries = new XYChart.Series<>();
        this.stackedBarChart = stackedBarChart;
        this.lineChart = line;
        this.stackedBarChart.setAnimated(false);
        this.lineChart.setAnimated(false);
    }

    public CustomSeries (StackedBarChart<String,Number> stackedBarChart) {
        this.series =  new XYChart.Series<>();
        this.lineSeries = new XYChart.Series<>();
        this.stackedBarChart = stackedBarChart;
        this.stackedBarChart.setAnimated(false);
    }

    public XYChart.Series<String, Number> getSeries() {
        return series;
    }

    public void setSeries(XYChart.Series<String, Number> series) {
        this.series = series;
    }

    public void setLineSeries (XYChart.Series<String,Number> series) {
        this.lineSeries = series;
    }

    public XYChart.Series<String, Number> getLineSeries() {
        return lineSeries;
    }

    public StackedBarChart<String, Number> getStackedBarChart() {
        return stackedBarChart;
    }

    public void setStackedBarChart(StackedBarChart<String, Number> stackedBarChart) {
        this.stackedBarChart = stackedBarChart;
    }

    public void deleteFromStack () {
        stackedBarChart.getData().remove(this.getSeries());
        lineChart.getData().remove(this.getLineSeries());

    }

    public void addToStack () {

        stackedBarChart.getData().add(this.getSeries());
        ArrayList<XYChart.Series<String,Number>> list = new ArrayList<>(stackedBarChart.getData().sorted());
        stackedBarChart.getData().clear();

        /*Workaround to fix java bug **/

        for (XYChart.Series<String,Number> i : list) {
            i.setNode(new StackPane());
        }

        stackedBarChart.getData().addAll(list);


        lineChart.getData().add(this.getLineSeries());
        ArrayList<XYChart.Series<String,Number>> list1 = new ArrayList<>(lineChart.getData().sorted());
        lineChart.getData().clear();
        lineChart.getData().addAll(list1);
    }
}


