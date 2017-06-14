package ui.views;

/* Copyright (C) 2017 Kevin Lopez Andrade <kevin@kevlopez.com>
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

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.layout.StackPane;
import ui.components.BackButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import electionresults.model.*;
import utils.DataManager;
import utils.ElectionsUtil;
import utils.IntegerCell;
import utils.StringCell;
import javafx.scene.control.SplitPane;

public class ZoneView extends SplitPane implements CustomView {

    @FXML
    private HBox backContainer;

    @FXML
    private ListView<String> provincesListView;

    @FXML
    private ListView<String> regionListView;

    @FXML
    private ListView<Integer> yearsListView;

    @FXML
    private ListView<String> communityListView;

    @FXML
    private TitledPane yearsPane;

    @FXML
    private TitledPane provincePane;

    @FXML
    private TitledPane regionPane;

    @FXML
    private TitledPane communityPane;

    @FXML
    private Accordion accordionId;

    @FXML
    private PieChart provincePieChart;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Label number;

    @FXML
    private Slider slider;

    private static final String name = "ZoneView";
    private ObservableList<String> allRegionsOb;
    private Map<String, ProvinceInfo> mapProvinceInfo;
    private List<Integer> electionYears;
    private List<String> allRegions;
    private List<String> communityList;
    private Boolean isFull;

    private enum Mode {
        COMMUNITY, PROVINCE, REGION
    }

    private Mode last;

    public ZoneView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Views/BarsView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        slider.setDisable(true);
        regionPane.setDisable(true);
        provincePane.setDisable(true);
        communityPane.setDisable(true);

    }

    public void updatePie(int year, Mode mode) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<PieChart.Data> pieChartData;
                pieChartData = FXCollections.observableArrayList();

                RegionResults regionResults = obtainRegionResults(year, mode);
                ArrayList<PartyResults> data = new ArrayList<>(regionResults.getPartyResultsSorted());

                for (PartyResults dat : data) {
                    String party;
                    int seats;
                    party = dat.getParty();
                    seats = dat.getSeats();

                    PieChart.Data aux = new PieChart.Data(party + " (" + seats + ")", seats);
                    if (seats > 0) pieChartData.add(aux);
                }
                ObservableList<PieChart.Data> finalPieChartData = pieChartData;
                Platform.runLater(() -> {
                    provincePieChart.setData(finalPieChartData);
                    updateTitles(year, Mode.PROVINCE);
                });
                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public void fillCharts(int year, Mode mode) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<PieChart.Data> pieChartData = null;

                if (mode != Mode.REGION) {
                    pieChartData = FXCollections.observableArrayList();
                }

                RegionResults regionResults = obtainRegionResults(year, mode);
                ArrayList<PartyResults> data = new ArrayList<>(regionResults.getPartyResultsSorted());
                ArrayList<XYChart.Series<String, Number>> seriesList = new ArrayList<>();

                for (PartyResults dat : data) {
                    XYChart.Series series;
                    String party;
                    int votes;
                    int seats;

                    votes = dat.getVotes();
                    party = dat.getParty();
                    seats = dat.getSeats();
                    series = new XYChart.Series();
                    series.setName(party);

                    XYChart.Data bar = new XYChart.Data("" + year, votes);
                    bar.setNode(new CustomStackPane(votes + ""));
                    series.getData().add(bar);


                    seriesList.add(series);

                    if (mode != Mode.REGION) {
                        PieChart.Data aux = new PieChart.Data(party + " (" + seats + ")", seats);
                        if (seats > 0) pieChartData.add(aux);
                    }
                }
                ObservableList<PieChart.Data> finalPieChartData = pieChartData;
                Platform.runLater(() -> {
                    if (mode != Mode.REGION) {
                        provincePieChart.setData(finalPieChartData);
                    }
                    barChart.getData().addAll(seriesList);
                    updateTitles(year, mode);
                });
                return null;
            }
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public synchronized void updateBarChart(int year, Mode mode, double value) {
        barChart.setAnimated(false);
        barChart.getData().clear();
        barChart.setAnimated(true);

        ArrayList<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
        RegionResults regionResults = obtainRegionResults(year, mode);

        ArrayList<PartyResults> data = new ArrayList<>(regionResults.getPartyResultsSorted());
        PollData pollData = regionResults.getPollData();

        int total = pollData.getVotes();
        for (PartyResults dat : data) {
            int votes = dat.getVotes();
            String party = dat.getParty();
            double aux = ((votes / (double) total) * 100);
            if (aux >= value) {
                XYChart.Series series = new XYChart.Series();
                series.setName(party);
                XYChart.Data bar = new XYChart.Data("" + year, votes);
                bar.setNode(new CustomStackPane(votes + ""));
                series.getData().add(bar);
                seriesList.add(series);
            }

        }
        barChart.getData().addAll(seriesList);
    }


    @FXML
    public void initialize() {

        ElectionsUtil.allowDeselectGeneric(provincesListView, new StringCell());
        ElectionsUtil.allowDeselectGeneric(communityListView, new StringCell());
        ElectionsUtil.allowDeselectGeneric(regionListView, new StringCell());
        ElectionsUtil.allowDeselectGeneric(yearsListView, new IntegerCell());

        yearsListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                yearsPane.setText(newValue.toString());
                communityPane.setDisable(false);
                updateAllByYear(newValue);
            } else {
                clearCharts();

                yearsPane.setText("Select Year");
                provincePane.setDisable(true);
                regionPane.setDisable(true);
                communityPane.setDisable(true);

                int communityIndex = communityListView.getSelectionModel().getSelectedIndex();
                communityListView.getSelectionModel().clearSelection(communityIndex);
                communityPane.setExpanded(false);

                int provinceIndex = provincesListView.getSelectionModel().getSelectedIndex();
                provincesListView.getSelectionModel().clearSelection(provinceIndex);
                provincePane.setExpanded(false);


                slider.setValue(0);

                int regionIndex = regionListView.getSelectionModel().getSelectedIndex();
                regionListView.getSelectionModel().clearSelection(regionIndex);
                regionPane.setExpanded(false);
            }
        }));

        communityListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                slider.setValue(0);
                communityPane.setText(newValue);
                provincePane.setDisable(false);
                slider.setDisable(false);
                clearCharts();
                isFull = true;
                fillCharts(yearsListView.getSelectionModel().getSelectedItem(), Mode.COMMUNITY);
            } else {
                clearCharts();

                communityPane.setText("Select Community");
                provincePane.setDisable(true);
                regionPane.setDisable(true);

                int yearIndex = yearsListView.getSelectionModel().getSelectedIndex();
                yearsListView.getSelectionModel().clearSelection(yearIndex);
                yearsPane.setExpanded(true);

                int provinceIndex = provincesListView.getSelectionModel().getSelectedIndex();
                provincesListView.getSelectionModel().clearSelection(provinceIndex);
                provincePane.setExpanded(false);

                slider.setValue(0);

                int regionIndex = regionListView.getSelectionModel().getSelectedIndex();
                regionListView.getSelectionModel().clearSelection(regionIndex);
                regionPane.setExpanded(false);
            }
        }));

        provincesListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                slider.setValue(0);
                isFull = false;
                ObservableList<String> observableList = FXCollections.observableArrayList(mapProvinceInfo.get(newValue).getRegions());
                regionListView.setItems(observableList);
                regionPane.setDisable(false);
                provincePane.setText(newValue);
                clearCharts();
                if (!isFull) {
                    fillCharts(yearsListView.getSelectionModel().getSelectedItem(), Mode.PROVINCE);
                    isFull = false;
                }
                System.out.println("Province");
            } else {
                clearCharts();

                provincePane.setText("Select Province");
                clearCharts();
                regionPane.setDisable(true);

                int regionIndex = regionListView.getSelectionModel().getSelectedIndex();
                regionListView.getSelectionModel().clearSelection(regionIndex);
                regionPane.setExpanded(false);

                slider.setValue(0);

                fillCharts(yearsListView.getSelectionModel().getSelectedItem(), Mode.COMMUNITY);
            }
        }));

        regionListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                slider.setValue(0);
                barChart.getData().clear();
                regionPane.setText(newValue);
                fillCharts(yearsListView.getSelectionModel().getSelectedItem(), Mode.REGION);
            } else {
                clearCharts();

                System.out.println("Region");
                isFull = true;
                fillCharts(yearsListView.getSelectionModel().getSelectedItem(), Mode.PROVINCE);
                slider.setValue(0);

                regionPane.setText("Selection Region");
            }

        }));

        slider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            number.setText(newValue.intValue() + "%");
            updateBarChart(yearsListView.getSelectionModel().getSelectedItem(), last, slider.getValue());
        }));
    }

    @Override
    public void refreshView() {
        if (communityList == null) {
            communityList = new ArrayList<>(1);
            communityList.add("C. Valenciana");
            ObservableList<String> community = FXCollections.observableArrayList(communityList);
            communityListView.setItems(community);
        }

        if (electionYears == null) {
            electionYears = DataManager.getElectionYears();
            ObservableList<Integer> observableListYears = FXCollections.observableArrayList(electionYears);
            yearsListView.setItems(observableListYears);
        }

        if (mapProvinceInfo == null) {
            mapProvinceInfo = DataManager.getElectionResults(electionYears.get(0)).getProvinces();
            ObservableList<String> provinces = FXCollections.observableArrayList(mapProvinceInfo.keySet());
            provincesListView.setItems(provinces);
        }

        if (allRegions == null) {
            allRegions = DataManager.getAllRegions();
            allRegionsOb = FXCollections.observableArrayList(allRegions);
            regionListView.setItems(allRegionsOb);
        }
    }

    public void fillData() {
        communityList = new ArrayList<>(1);
        communityList.add("C. Valenciana");
        ObservableList<String> community = FXCollections.observableArrayList(communityList);
        communityListView.setItems(community);

        electionYears = DataManager.getElectionYears();
        ObservableList<Integer> observableListYears = FXCollections.observableArrayList(electionYears);
        yearsListView.setItems(observableListYears);

        mapProvinceInfo = DataManager.getElectionResults(electionYears.get(0)).getProvinces();
        ObservableList<String> provinces = FXCollections.observableArrayList(mapProvinceInfo.keySet());
        provincesListView.setItems(provinces);

        allRegions = DataManager.getAllRegions();
        allRegionsOb = FXCollections.observableArrayList(allRegions);
        regionListView.setItems(allRegionsOb);
    }

    public void updateAllByYear(int year) {
        clearCharts();
        boolean community = communityListView.getSelectionModel().isEmpty();
        boolean province = provincesListView.getSelectionModel().isEmpty();
        boolean region = regionListView.getSelectionModel().isEmpty();

        if (community) {
            return;
        }

        if (region && province) {
            fillCharts(year, Mode.COMMUNITY);
            return;
        }

        if (!region && !province) {
            fillCharts(year, Mode.REGION);
            updatePie(year, Mode.PROVINCE);
            return;
        }
        if (!province && region) {
            fillCharts(year, Mode.PROVINCE);
        }
    }

    public void clearCharts() {
        provincePieChart.setAnimated(false);
        provincePieChart.getData().clear();
        provincePieChart.setAnimated(true);
        provincePieChart.setTitle("");

        barChart.setAnimated(false);
        barChart.getData().clear();
        barChart.setTitle("");
        barChart.setAnimated(true);
    }

    public RegionResults obtainRegionResults(int year, Mode mode) {
        ElectionResults electionResults = DataManager.getElectionResults(year);
        RegionResults regionResults = null;
        switch (mode) {
            case COMMUNITY:
                regionResults = electionResults.getGlobalResults();
                last = Mode.COMMUNITY;
                break;
            case PROVINCE:
                regionResults = electionResults.getProvinceResults(provincesListView.getSelectionModel().getSelectedItem());
                last = Mode.PROVINCE;
                break;
            case REGION:
                regionResults = electionResults.getRegionResults(regionListView.getSelectionModel().getSelectedItem());
                last = Mode.REGION;
                break;
        }
        return regionResults;
    }

    public void updateTitles(int year, Mode mode) {

        switch (mode) {
            case COMMUNITY:
                String community = communityListView.getSelectionModel().selectedItemProperty().get();
                provincePieChart.setTitle("Seats distribution for " + community + " " + year);
                barChart.setTitle(community + " " + year);
                break;
            case PROVINCE:
                String province = provincesListView.getSelectionModel().selectedItemProperty().get();
                provincePieChart.setTitle("Seats distribution for " + province + " " + year);
                barChart.setTitle(province + " " + year);
                break;
            case REGION:
                String region = regionListView.getSelectionModel().selectedItemProperty().get();
                barChart.setTitle(region + " " + year);
                break;
        }

    }

    @Override
    public void setBackButton(BackButton button) {

        if (backContainer.getChildren().size() == 0) {
            backContainer.getChildren().add(button);
        }
    }

    @Override
    public String name() {
        return name;
    }


    public static String getName() {
        return name;
    }

    class CustomStackPane extends StackPane {

        @FXML
        private Label label;

        public CustomStackPane(String label) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Components/CustomStackPane.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);

            try {
                fxmlLoader.load();

            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            this.label.setText(label);

        }
    }

}
