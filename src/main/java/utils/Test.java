package utils;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;

/* Test.java
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
 */

public class Test extends Application {

    final static String austria = "Austria";
    final static String brazil = "Brazil";
    final static String france = "France";
    final static String italy = "Italy";
    final static String usa = "USA";


    @Override
    public void start(Stage stage) {
        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Button buttonRemove1 =  new Button("Remove 2003");
        Button buttonRemove2 =  new Button("Remove 2004");
        Button buttonRemove3 =  new Button("Remove 2005");

        Button buttonAdd1 =  new Button("Add 2003");
        Button buttonAdd2 =  new Button("Add 2004");
        Button buttonAdd3 =  new Button("Add 2005");

        hBox.getChildren().addAll(buttonRemove1,buttonRemove2,buttonRemove3,buttonAdd1,buttonAdd2,buttonAdd3);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        StackedBarChart<String, Number> sbc = new StackedBarChart<>(xAxis, yAxis);

        CustomSeries customSeries1 = new CustomSeries(sbc);
        XYChart.Series<String, Number> series1 = customSeries1.getSeries();

        CustomSeries customSeries2 = new CustomSeries (sbc);
        XYChart.Series<String, Number> series2 = customSeries2.getSeries();

        CustomSeries customSeries3 = new CustomSeries (sbc);
        XYChart.Series<String, Number> series3 = customSeries3.getSeries();

        buttonRemove1.setOnAction((e)->{
            customSeries1.deleteFromStack();
        });

        buttonRemove2.setOnAction((e)->{
            customSeries2.deleteFromStack();
        });

        buttonRemove3.setOnAction((e)->{
            customSeries3.deleteFromStack();
        });

        buttonAdd1.setOnAction((e)->{
            customSeries1.addToStack();
        });

        buttonAdd2.setOnAction((e)->{
            customSeries2.addToStack();
        });

        buttonAdd3.setOnAction((e)->{
            customSeries3.addToStack();
        });

        sbc.setTitle("Country Summary");
        xAxis.setLabel("Country");

        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(austria, brazil, france, italy, usa)));

        yAxis.setLabel("Value");
        series1.setName("2003");
        series1.getData().add(new XYChart.Data<String, Number>(austria, 25601.34));
        series1.getData().add(new XYChart.Data<String, Number>(brazil, 20148.82));
        series1.getData().add(new XYChart.Data<String, Number>(france, 10000));
        series1.getData().add(new XYChart.Data<String, Number>(italy, 35407.15));
        series1.getData().add(new XYChart.Data<String, Number>(usa, 12000));

        series2.setName("2004");
        series2.getData().add(new XYChart.Data<String, Number>(austria, 57401.85));
        series2.getData().add(new XYChart.Data<String, Number>(brazil, 41941.19));
        series2.getData().add(new XYChart.Data<String, Number>(france, 45263.37));
        series2.getData().add(new XYChart.Data<String, Number>(italy, 117320.16));
        series2.getData().add(new XYChart.Data<String, Number>(usa, 14845.27));

        series3.setName("2005");
        series3.getData().add(new XYChart.Data<String, Number>(austria, 45000.65));
        series3.getData().add(new XYChart.Data<String, Number>(brazil, 44835.76));
        series3.getData().add(new XYChart.Data<String, Number>(france, 18722.18));
        series3.getData().add(new XYChart.Data<String, Number>(italy, 17557.31));
        series3.getData().add(new XYChart.Data<String, Number>(usa, 92633.68));

        vBox.getChildren().addAll(hBox,sbc);
        Scene scene = new Scene(vBox, 800, 600);

        sbc.getData().addAll(series1, series2, series3);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
