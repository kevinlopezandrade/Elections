package ui.views;

/* WelcomeView.java
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

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.util.Duration;
import ui.components.BackButton;
import javafx.scene.Node;
import javafx.animation.Interpolator;
import javafx.scene.layout.Pane;

public class WelcomeView extends HBox implements CustomView{

	
	@FXML
	private HBox iconsContainer;

	@FXML
	private VBox zoneIconContainer;

	@FXML
	private Button zoneButton;

	@FXML
	private VBox historicIconContainer;

	@FXML
	private Button historicButton;

	@FXML
	private HBox buttonsContainer;


	private static final String name = "WelcomeView";

	public WelcomeView () {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Views/WelcomeView.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}

	public static String getName() {
		return name;
	}



	@Override
	public void refreshView() {
	}

	@FXML
	public void zonePressed (ActionEvent event) {
		BackButton backButton = new BackButton(this);
		ZoneView zoneView = new ZoneView();
		zoneView.fillData();
        zoneView.setBackButton(backButton);
		MainContainer.getMainContainer().changeViewTo((Node)zoneView);
	}

	@FXML
	public void historicPressed (ActionEvent event) {
		BackButton backButton = new BackButton(this);
		HistoricView historicView = new HistoricView();
		historicView.fillData();
		historicView.fillSeats();
		historicView.setBackButton(backButton);
		MainContainer.getMainContainer().changeViewTo((Node) historicView);
	}

	@Override
	public void setBackButton (BackButton button) {

	}

	@Override
	public String name () {
		return name;	
	}

	public static void removeFadingOut(final Node node, final Pane parent) {
		if (parent.getChildren().contains(node)) {
			FadeTransition transition = new FadeTransition(Duration.millis(250), node);
			transition.setFromValue(node.getOpacity());
			transition.setToValue(0);
			transition.setInterpolator(Interpolator.EASE_BOTH);
			transition.setOnFinished(finishHim -> {
				parent.getChildren().remove(node);
			});
			transition.play();
		}
	}

}
