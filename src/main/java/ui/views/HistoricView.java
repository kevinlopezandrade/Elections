package ui.views;

/* HistoricView.java
 *
 * Copyright (C) 2017 Kevin Lopez Andrade <kevin@kevlopez.com>
 * Vicent Dolz Martinez <vicentdolz@hotmail.com>
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


import electionresults.model.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.components.BackButton;
import ui.components.CustomCheckBox;
import utils.CustomSeries;
import utils.DataManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoricView extends SplitPane implements CustomView {


    @FXML
    private HBox backContainer;

    @FXML
    private LineChart<String, Number> histLineChart;

    @FXML
    private StackedBarChart<String,Number> histStackChart;

    @FXML
    private VBox auxVBox;

    @FXML
    private BarChart<String, Integer> historicBarChart;
    ArrayList<XYChart.Series<String,Number>> sList = new ArrayList<>();


    private static final String name = "HistoricView";
    private List<Integer> years = DataManager.getElectionYears();

    public HistoricView () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Views/Historic.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void refreshView() {

    }

    public void fillData() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                XYChart.Series comunity = new XYChart.Series();
                XYChart.Series alicante = new XYChart.Series();
                XYChart.Series valencia = new XYChart.Series();
                XYChart.Series castellon = new XYChart.Series();

                Platform.runLater(()->{
                    historicBarChart.setTitle("Participation (%)");
                });

                comunity.setName("Comunitat Valenciana");

                alicante.setName("Alicante");
                castellon.setName("Castellon");
                valencia.setName("Valencia");

                List<Integer> years = DataManager.getElectionYears();


                for(int year : years){
                    ElectionResults elections = DataManager.getElectionResults(year);

                    PollData datos = elections.getGlobalResults().getPollData();
                    double censo = datos.getCensus();
                    double votos = datos.getVotes();
                    double pers = votos / censo;
                    pers = pers*100;

                    comunity.getData().add(new XYChart.Data(""+year,pers));


                    PollData datosA = elections.getProvinceResults("Alicante").getPollData();

                    censo = datosA.getCensus();
                    votos = datosA.getVotes();
                    pers = votos / censo;
                    pers = pers*100;
                    alicante.getData().add(new XYChart.Data(""+year,pers));


                    PollData datosC = elections.getProvinceResults("Castellón").getPollData();

                    censo = datosC.getCensus();
                    votos = datosC.getVotes();
                    pers = votos / censo;
                    pers = pers*100;
                    castellon.getData().add(new XYChart.Data(""+year,pers));

                    PollData datosV = elections.getProvinceResults("Valencia").getPollData();

                    censo = datosV.getCensus();
                    votos = datosV.getVotes();
                    pers = votos / censo;
                    pers = pers*100;
                    valencia.getData().add(new XYChart.Data(""+year,pers));

                }

                Platform.runLater(()->{
                    historicBarChart.getData().addAll(comunity,alicante,castellon,valencia);
                });
                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public void fillSeats () {
            for (Party party : Party.values()) {

                CustomSeries series = new CustomSeries(histStackChart,histLineChart);
                series.setSeries(boxClicked(party.getName()));
                series.setLineSeries(ListBoxClicked(party.getName()));


                CustomCheckBox checkBox;
                checkBox = new CustomCheckBox();
                checkBox.setSeries(series);
                checkBox.setText(party.getName());

                checkBox.setPrefWidth(120);
                checkBox.setStyle("-fx-text-fill: white");

                Insets ins = new Insets(0.0, 0.0, 0.0, 5.0);
                checkBox.setPadding(ins);
                ImageView logo = new ImageView(party.getLogo());

                logo.setFitWidth(35);
                logo.setFitHeight(35);


                HBox hBox = new HBox();
                hBox.setSpacing(15);
                hBox.setPrefHeight(10.0);
                hBox.setAlignment(Pos.TOP_LEFT);
                hBox.getChildren().addAll(logo, checkBox);
                auxVBox.getChildren().add(hBox);
            }


    }

    @Override
    public void setBackButton(BackButton button) {
        if(backContainer.getChildren().size() == 0 ) {
            backContainer.getChildren().add(button);
        }
    }

    private XYChart.Series<String,Number> boxClicked(String par){

        List<String> lista = Party.getPartyByName(par).getAcronyms();

        XYChart.Series series = new XYChart.Series();
        series.setName(par);


        for(int year: years){
            ElectionResults comunityResults = DataManager.getElectionResults(year);
            RegionResults regionResults = comunityResults.getGlobalResults();
            Map<String, String> map = comunityResults.getPartyNames();
            List<String> names = new ArrayList<>(map.keySet());
            String correctParty = null;

            for(String i : lista) {
                for(String x : names) {
                    if(i.compareTo(x) == 0) {
                        correctParty = x;
                    }
                }
            }

            if(year == 2011) {
                if(par.compareTo("PP") == 0) {
                    correctParty = "PP";
                }
            }
            if(correctParty != null) {
                PartyResults partyResults = regionResults.getPartyResults(correctParty);
                int seats = partyResults.getSeats();
                series.getData().add(new XYChart.Data(""+year, seats));
            }
        }
        return series;

    }

    private XYChart.Series<String,Number> ListBoxClicked(String par){

        List<String> lista = Party.getPartyByName(par).getAcronyms();

        XYChart.Series series = new XYChart.Series();
        series.setName(par);


        for(int year: years){
            ElectionResults comunityResults = DataManager.getElectionResults(year);
            RegionResults regionResults = comunityResults.getGlobalResults();
            Map<String, String> map = comunityResults.getPartyNames();
            List<String> names = new ArrayList<>(map.keySet());
            String correctParty = null;

            for(String i : lista) {
                for(String x : names) {
                    if(i.compareTo(x) == 0) {
                        correctParty = x;
                    }
                }
            }


            if(year == 2011) {
                if(par.compareTo("PP") == 0) {
                    correctParty = "PP";
                }
            }
            if(correctParty != null) {
                PartyResults partyResults = regionResults.getPartyResults(correctParty);
                int votes = partyResults.getVotes();
                series.getData().add(new XYChart.Data(""+year, votes));
            }
        }
        return series;

    }



    @Override
    public String name() {
        return name;
    }

    public static String getName () {
        return name;
    }

}
