# Elections APP

Simple JavaFx app to see election results in Valencia.



## Screenshots

![Screen Shot 2017-06-14 at 01.42.11](http://i.imgur.com/YBCmNrO.png)

![Screen Shot 2017-06-14 at 01.42.22](http://imgur.com/Tr6Vrc1.png)

![Screen Shot 2017-06-14 at 01.46.43](http://imgur.com/oK4QaAv.png)



## Structure of the project

```
.
|-- README.md
|-- build.gradle
`-- src
    |-- main
    |   |-- java
    |   |   |-- app
    |   |   |   `-- ElectionsApp.java
    |   |   |-- ui
    |   |   |   |-- components
    |   |   |   |   |-- BackButton.java
    |   |   |   |   |-- CustomCheckBox.java
    |   |   |   |   `-- ProgressCircle.java
    |   |   |   `-- views
    |   |   |       |-- CustomView.java
    |   |   |       |-- HistoricView.java
    |   |   |       |-- MainContainer.java
    |   |   |       |-- WelcomeView.java
    |   |   |       `-- ZoneView.java
    |   |   `-- utils
    |   |       |-- CustomSeries.java
    |   |       |-- DataManager.java
    |   |       |-- ElectionsUtil.java
    |   |       |-- IntegerCell.java
    |   |       |-- StringCell.java
    |   |       `-- Test.java
    |   `-- resources
    |       |-- FXML
    |       |   |-- Components
    |       |   |   |-- BackButton.fxml
    |       |   |   |-- CustomStackPane.fxml
    |       |   |   `-- ProgressCircle.fxml
    |       |   `-- Views
    |       |       |-- BarsView.fxml
    |       |       |-- Historic.fxml
    |       |       |-- MainContainer.fxml
    |       |       `-- WelcomeView.fxml
    |       |-- css
    |       |   `-- Main.css
    |       |-- icons
    |       |   |-- BackButtonWhite.png
    |       |   |-- HistoricIcon.png
    |       |   `-- ZoneIcon.png
    |       `-- libs
    |           `-- Elections.jar
    `-- test
        |-- java
        `-- resources
```

